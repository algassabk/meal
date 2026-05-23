$ErrorActionPreference = "Stop"

$base = "http://localhost:8080"
$results = New-Object System.Collections.Generic.List[object]
$failures = 0

function Add-Result($name, $method, $url, $status, $ok, $note) {
    $script:results.Add([PSCustomObject]@{
        Name = $name
        Method = $method
        Url = $url
        Status = $status
        OK = $ok
        Note = $note
    }) | Out-Null

    if (-not $ok) {
        $script:failures++
    }
}

function Invoke-Api($name, $method, $path, $body = $null, $token = $null, $expected = @(200)) {
    $headers = @{}

    if ($token) {
        $headers.Authorization = "Bearer $token"
    }

    try {
        if ($null -ne $body) {
            $json = $body | ConvertTo-Json -Depth 20
            $response = Invoke-WebRequest -Uri "$base$path" -Method $method -ContentType "application/json" -Body $json -Headers $headers -UseBasicParsing
        } else {
            $response = Invoke-WebRequest -Uri "$base$path" -Method $method -Headers $headers -UseBasicParsing
        }

        $content = [string]$response.Content
        $note = $content.Substring(0, [Math]::Min(120, $content.Length))
        Add-Result $name $method $path $response.StatusCode ($expected -contains [int]$response.StatusCode) $note

        if ($content -and ($content.Trim().StartsWith("{") -or $content.Trim().StartsWith("["))) {
            return $content | ConvertFrom-Json
        }

        return $content
    } catch {
        $status = 0
        $content = $_.Exception.Message

        if ($_.Exception.Response) {
            $status = [int]$_.Exception.Response.StatusCode
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $content = $reader.ReadToEnd()
        }

        Add-Result $name $method $path $status ($expected -contains $status) $content
        return $null
    }
}

$ts = [DateTimeOffset]::Now.ToUnixTimeMilliseconds()
$userEmail = "flow-user-$ts@example.com"
$adminEmail = "flow-admin-$ts@example.com"
$password = "password123"

Invoke-Api "Auth - Register user" "POST" "/auth/register" @{ fullName = "Flow User"; email = $userEmail; password = $password } | Out-Null
$login = Invoke-Api "Auth - Login user" "POST" "/auth/login" @{ email = $userEmail; password = $password }
$userToken = $login.token

Invoke-Api "Auth - Register admin seed user" "POST" "/auth/register" @{ fullName = "Flow Admin"; email = $adminEmail; password = $password } | Out-Null
$env:PGPASSWORD = "12345678"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -h localhost -U postgres -d meal_db -c "UPDATE users SET role = 'ADMIN' WHERE email = '$adminEmail';" | Out-Null
$adminLogin = Invoke-Api "Auth - Login admin" "POST" "/auth/login" @{ email = $adminEmail; password = $password }
$adminToken = $adminLogin.token

Invoke-Api "Users - Me" "GET" "/users/me" $null $userToken | Out-Null
Invoke-Api "Users - Profile" "GET" "/users/profile" $null $userToken | Out-Null
Invoke-Api "Security - Users without JWT rejected" "GET" "/users/me" $null $null @(401, 403) | Out-Null

$category = Invoke-Api "Categories - Create" "POST" "/categories" @{ name = "Dinner $ts"; description = "Dinner recipes" }
Invoke-Api "Categories - List" "GET" "/categories" | Out-Null
Invoke-Api "Categories - Get by id" "GET" "/categories/$($category.id)" | Out-Null
Invoke-Api "Categories - Update" "PUT" "/categories/$($category.id)" @{ name = "Dinner Updated $ts"; description = "Updated" } | Out-Null
Invoke-Api "Validation - Category requires name" "POST" "/categories" @{ name = ""; description = "bad" } $null @(400) | Out-Null

$ingredient = Invoke-Api "Ingredients - Create" "POST" "/ingredients" @{ name = "Tomato $ts"; unit = "piece" }
Invoke-Api "Ingredients - List" "GET" "/ingredients" | Out-Null
Invoke-Api "Ingredients - Get by id" "GET" "/ingredients/$($ingredient.id)" | Out-Null
$ingredientUpdated = Invoke-Api "Ingredients - Update" "PUT" "/ingredients/$($ingredient.id)" @{ name = "Tomato Updated $ts"; unit = "gram" }
Add-Result "Ingredients - Update unit value check" "CHECK" "local" 200 ($ingredientUpdated.unit -eq "gram") "unit should update"

$recipeBody = @{ title = "Flow Recipe $ts"; description = "A tested recipe"; instructions = "Mix and cook"; prepTime = 10; cookTime = 20; servings = 2; imageUrl = ""; categoryId = $category.id }
Invoke-Api "Security - Create recipe without JWT rejected" "POST" "/recipes" $recipeBody $null @(401, 403) | Out-Null
$recipe = Invoke-Api "Recipes - Create" "POST" "/recipes" $recipeBody $userToken
Invoke-Api "Recipes - List" "GET" "/recipes" | Out-Null
Invoke-Api "Recipes - Get by id" "GET" "/recipes/$($recipe.id)" | Out-Null
Invoke-Api "Recipes - Update" "PUT" "/recipes/$($recipe.id)" @{ title = "Flow Recipe Updated $ts"; description = "Updated"; instructions = "Cook again"; prepTime = 11; cookTime = 21; servings = 3; imageUrl = ""; categoryId = $category.id } $userToken | Out-Null
Invoke-Api "Recipes - Visibility" "PUT" "/recipes/$($recipe.id)/visibility?isPublic=true" $null $userToken | Out-Null
Invoke-Api "Recipes - Search" "GET" "/recipes/search?keyword=Flow" | Out-Null
Invoke-Api "Recipes - Search paged" "GET" "/recipes/search/paged?keyword=Flow&page=0&size=5" | Out-Null
Invoke-Api "Recipes - Category filter" "GET" "/recipes/category/$($category.id)" | Out-Null
Invoke-Api "Recipes - Category filter paged" "GET" "/recipes/category/$($category.id)/paged?page=0&size=5" | Out-Null
Invoke-Api "Recipes - Paged" "GET" "/recipes/paged?page=0&size=5&sortBy=createdAt&direction=desc" | Out-Null
Invoke-Api "Validation - Recipe requires title" "POST" "/recipes" @{ title = ""; instructions = "Cook"; servings = 1; categoryId = $category.id } $userToken @(400) | Out-Null

$recipeIngredient = Invoke-Api "Recipe Ingredients - Add" "POST" "/recipes/$($recipe.id)/ingredients/$($ingredient.id)" @{ quantity = 2.5 } $userToken
Invoke-Api "Recipe Ingredients - List" "GET" "/recipes/$($recipe.id)/ingredients" | Out-Null
Invoke-Api "Recipe Ingredients - Update" "PUT" "/recipes/$($recipe.id)/ingredients/$($recipeIngredient.id)/$($ingredient.id)" @{ quantity = 3.0 } $userToken | Out-Null

$favorite = Invoke-Api "Favorites - Add" "POST" "/favorites/$($recipe.id)" $null $userToken
Invoke-Api "Favorites - List" "GET" "/favorites" $null $userToken | Out-Null
Invoke-Api "Security - Favorites without JWT rejected" "GET" "/favorites" $null $null @(401, 403) | Out-Null

$comment = Invoke-Api "Comments - Add" "POST" "/comments/recipe/$($recipe.id)" @{ commentText = "Looks tasty" } $userToken
Invoke-Api "Comments - List by recipe" "GET" "/comments/recipe/$($recipe.id)" | Out-Null
Invoke-Api "Comments - Update" "PUT" "/comments/$($comment.id)" @{ commentText = "Looks very tasty" } $userToken | Out-Null

$mealPlan = Invoke-Api "Meal Plans - Create" "POST" "/meal-plans" @{ title = "Weekly Plan $ts"; startDate = "2026-05-23"; endDate = "2026-05-30" } $userToken
Invoke-Api "Meal Plans - List mine" "GET" "/meal-plans" $null $userToken | Out-Null
Invoke-Api "Meal Plans - Get by id" "GET" "/meal-plans/$($mealPlan.id)" $null $userToken | Out-Null
Invoke-Api "Meal Plans - Update" "PUT" "/meal-plans/$($mealPlan.id)" @{ title = "Weekly Plan Updated $ts"; startDate = "2026-05-24"; endDate = "2026-05-31" } $userToken | Out-Null
$mealPlanItem = Invoke-Api "Meal Plan Items - Add recipe" "POST" "/meal-plan-items/$($mealPlan.id)/$($recipe.id)" @{ plannedDate = "2026-05-24"; mealType = "DINNER" } $userToken
Invoke-Api "Meal Plan Items - List" "GET" "/meal-plan-items/$($mealPlan.id)" $null $userToken | Out-Null

$shoppingList = Invoke-Api "Shopping Lists - Create" "POST" "/shopping-lists" @{ title = "Shopping $ts"; mealPlan = @{ id = $mealPlan.id } } $userToken
Invoke-Api "Shopping Lists - List mine" "GET" "/shopping-lists" $null $userToken | Out-Null
Invoke-Api "Shopping Lists - Get by id" "GET" "/shopping-lists/$($shoppingList.id)" $null $userToken | Out-Null
Invoke-Api "Shopping Lists - Update" "PUT" "/shopping-lists/$($shoppingList.id)" @{ title = "Shopping Updated $ts" } $userToken | Out-Null
$shoppingListItem = Invoke-Api "Shopping List Items - Add" "POST" "/shopping-list-items/$($shoppingList.id)/$($ingredient.id)" @{ quantity = 4; unit = "piece"; isChecked = $false } $userToken
Invoke-Api "Shopping List Items - List" "GET" "/shopping-list-items/$($shoppingList.id)" $null $userToken | Out-Null
$shoppingListItemUpdated = Invoke-Api "Shopping List Items - Update" "PUT" "/shopping-list-items/$($shoppingListItem.id)" @{ quantity = 5; unit = "box"; isChecked = $true } $userToken
Add-Result "Shopping List Items - Update value check" "CHECK" "local" 200 ($shoppingListItemUpdated.unit -eq "box" -and $shoppingListItemUpdated.isChecked -eq $true) "unit and isChecked should update"

$verification = Invoke-Api "Email Verification - Request" "POST" "/auth/email-verification/request" @{ email = $userEmail }
$verificationToken = ($verification -replace "Email verification token: ", "")
Invoke-Api "Email Verification - Verify" "GET" "/auth/email-verification/verify?token=$verificationToken" | Out-Null
$reset = Invoke-Api "Password Reset - Request" "POST" "/auth/password-reset/request" @{ email = $userEmail }
$resetToken = ($reset -replace "Password reset token: ", "")
Invoke-Api "Password Reset - Confirm" "POST" "/auth/password-reset/confirm" @{ token = $resetToken; newPassword = "newpass123" } | Out-Null
Invoke-Api "Password Reset - Login with new password" "POST" "/auth/login" @{ email = $userEmail; password = "newpass123" } | Out-Null

Invoke-Api "Admin - Pending recipes" "GET" "/admin/recipes/pending" $null $adminToken | Out-Null
Invoke-Api "Security - Admin requires ADMIN" "GET" "/admin/recipes/pending" $null $userToken @(403) | Out-Null
Invoke-Api "Admin - Approve recipe" "PUT" "/admin/recipes/$($recipe.id)/approve" $null $adminToken | Out-Null

$imagePath = Join-Path (Get-Location) "target\api-smoke-upload.txt"
Set-Content -Path $imagePath -Value "fake image content"
try {
    $upload = & curl.exe -s -w "`n%{http_code}" -X POST "$base/recipes/$($recipe.id)/image" -H "Authorization: Bearer $userToken" -F "file=@$imagePath;type=image/png"
    $lines = $upload -split "`n"
    $status = [int]$lines[-1]
    $body = ($lines[0..($lines.Length - 2)] -join "`n")
    Add-Result "Image Upload - Upload recipe image" "POST" "/recipes/$($recipe.id)/image" $status ($status -eq 200) $body.Substring(0, [Math]::Min(120, $body.Length))
    $uploaded = $body | ConvertFrom-Json
    Invoke-Api "Image Upload - Static file accessible" "GET" $uploaded.imageUrl | Out-Null
} finally {
    Remove-Item -LiteralPath $imagePath -ErrorAction SilentlyContinue
}

Invoke-Api "Swagger - API Docs" "GET" "/v3/api-docs" | Out-Null
Invoke-Api "Swagger - UI" "GET" "/swagger-ui.html" $null $null @(200, 302) | Out-Null

Invoke-Api "Shopping List Items - Delete" "DELETE" "/shopping-list-items/$($shoppingListItem.id)" $null $userToken | Out-Null
Invoke-Api "Shopping Lists - Delete" "DELETE" "/shopping-lists/$($shoppingList.id)" $null $userToken | Out-Null
Invoke-Api "Meal Plan Items - Delete" "DELETE" "/meal-plan-items/$($mealPlanItem.id)" $null $userToken | Out-Null
Invoke-Api "Meal Plans - Delete" "DELETE" "/meal-plans/$($mealPlan.id)" $null $userToken | Out-Null
Invoke-Api "Favorites - Delete" "DELETE" "/favorites/$($favorite.id)" $null $userToken | Out-Null
Invoke-Api "Comments - Delete" "DELETE" "/comments/$($comment.id)" $null $userToken | Out-Null
Invoke-Api "Recipe Ingredients - Delete" "DELETE" "/recipes/$($recipe.id)/ingredients/$($recipeIngredient.id)" $null $userToken | Out-Null
Invoke-Api "Admin - Delete recipe" "DELETE" "/admin/recipes/$($recipe.id)" $null $adminToken | Out-Null

$results | Format-Table -AutoSize | Out-String -Width 220

if ($failures -gt 0) {
    Write-Host "FAILURES: $failures"
    exit 1
}

Write-Host "ALL_ENDPOINT_SMOKE_TESTS_PASSED"
