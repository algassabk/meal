# Meal API Testing Guide

Base URL:

```text
http://localhost:8080
```

Use this order in Postman. Save the login token as `userToken`, and for admin tests save an admin login token as `adminToken`.

## 1. Register User

- Method: `POST`
- URL: `/auth/register`
- Auth: No
- Body:

```json
{
  "fullName": "Test User",
  "email": "testuser@example.com",
  "password": "password123"
}
```

- Expected: `200 OK`, response contains `token`.

## 2. Login User

- Method: `POST`
- URL: `/auth/login`
- Auth: No
- Body:

```json
{
  "email": "testuser@example.com",
  "password": "password123"
}
```

- Expected: `200 OK`, response contains `token`.
- Save token as Bearer token for protected routes.

## 3. Current User

- Method: `GET`
- URL: `/users/me`
- Auth: Bearer `userToken`
- Expected: `200 OK`, current user object.

## 4. Create Category

- Method: `POST`
- URL: `/categories`
- Auth: No
- Body:

```json
{
  "name": "Dinner",
  "description": "Dinner recipes"
}
```

- Expected: `200 OK`, save `id` as `categoryId`.

## 5. Create Ingredient

- Method: `POST`
- URL: `/ingredients`
- Auth: No
- Body:

```json
{
  "name": "Tomato",
  "unit": "piece"
}
```

- Expected: `200 OK`, save `id` as `ingredientId`.

## 6. Create Recipe

- Method: `POST`
- URL: `/recipes`
- Auth: Bearer `userToken`
- Body:

```json
{
  "title": "Tomato Pasta",
  "description": "Simple pasta recipe",
  "instructions": "Boil pasta and cook sauce.",
  "prepTime": 10,
  "cookTime": 20,
  "servings": 2,
  "imageUrl": "",
  "categoryId": 1
}
```

- Expected: `200 OK`, save `id` as `recipeId`.

## 7. Add Ingredient To Recipe

- Method: `POST`
- URL: `/recipes/{{recipeId}}/ingredients/{{ingredientId}}`
- Auth: Bearer `userToken`
- Body:

```json
{
  "quantity": 2
}
```

- Expected: `200 OK`, save `id` as `recipeIngredientId`.

## 8. Favorite Recipe

- Method: `POST`
- URL: `/favorites/{{recipeId}}`
- Auth: Bearer `userToken`
- Expected: `200 OK`, save `id` as `favoriteId`.

## 9. Add Comment

- Method: `POST`
- URL: `/comments/recipe/{{recipeId}}`
- Auth: Bearer `userToken`
- Body:

```json
{
  "commentText": "Looks delicious!"
}
```

- Expected: `200 OK`, save `id` as `commentId`.

## 10. Create Meal Plan

- Method: `POST`
- URL: `/meal-plans`
- Auth: Bearer `userToken`
- Body:

```json
{
  "title": "Weekly Meal Plan",
  "startDate": "2026-05-23",
  "endDate": "2026-05-30"
}
```

- Expected: `200 OK`, save `id` as `mealPlanId`.

## 11. Add Recipe To Meal Plan

- Method: `POST`
- URL: `/meal-plan-items/{{mealPlanId}}/{{recipeId}}`
- Auth: Bearer `userToken`
- Body:

```json
{
  "plannedDate": "2026-05-24",
  "mealType": "DINNER"
}
```

- Expected: `200 OK`, save `id` as `mealPlanItemId`.

## 12. Create Shopping List

- Method: `POST`
- URL: `/shopping-lists`
- Auth: Bearer `userToken`
- Body:

```json
{
  "title": "Weekly Shopping List",
  "mealPlan": {
    "id": 1
  }
}
```

- Expected: `200 OK`, save `id` as `shoppingListId`.

## 13. Add Shopping List Item

- Method: `POST`
- URL: `/shopping-list-items/{{shoppingListId}}/{{ingredientId}}`
- Auth: Bearer `userToken`
- Body:

```json
{
  "quantity": 4,
  "unit": "piece",
  "isChecked": false
}
```

- Expected: `200 OK`, save `id` as `shoppingListItemId`.

## 14. Search Recipes

- Method: `GET`
- URL: `/recipes/search?keyword=Tomato`
- Auth: No
- Expected: `200 OK`, list of matching recipes.

## 15. Filter Recipes By Category

- Method: `GET`
- URL: `/recipes/category/{{categoryId}}`
- Auth: No
- Expected: `200 OK`, list of recipes.

## 16. Test Pagination

- Method: `GET`
- URL: `/recipes/paged?page=0&size=5&sortBy=createdAt&direction=desc`
- Auth: No
- Expected: `200 OK`, page response with `content`.

## 17. Image Upload

- Method: `POST`
- URL: `/recipes/{{recipeId}}/image`
- Auth: Bearer `userToken`
- Body: `form-data`
- Key: `file`
- Type: File
- Expected: `200 OK`, recipe response contains `imageUrl`.
- Test image URL with `GET /uploads/{fileName}`.

## 18. Email Verification Request

- Method: `POST`
- URL: `/auth/email-verification/request`
- Auth: No
- Body:

```json
{
  "email": "testuser@example.com"
}
```

- Expected: `200 OK`, response contains verification token.

## 19. Verify Email

- Method: `GET`
- URL: `/auth/email-verification/verify?token={{verificationToken}}`
- Auth: No
- Expected: `200 OK`, `Email verified successfully`.

## 20. Password Reset Request

- Method: `POST`
- URL: `/auth/password-reset/request`
- Auth: No
- Body:

```json
{
  "email": "testuser@example.com"
}
```

- Expected: `200 OK`, response contains password reset token.

## 21. Password Reset Confirm

- Method: `POST`
- URL: `/auth/password-reset/confirm`
- Auth: No
- Body:

```json
{
  "token": "paste-token-here",
  "newPassword": "newpass123"
}
```

- Expected: `200 OK`, `Password reset successfully`.

## 22. Admin Setup

Register an admin test user, then update the database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

Login as that admin and save the JWT as `adminToken`.

## 23. Admin Pending Recipes

- Method: `GET`
- URL: `/admin/recipes/pending`
- Auth: Bearer `adminToken`
- Expected: `200 OK`.

## 24. Admin Approve Recipe

- Method: `PUT`
- URL: `/admin/recipes/{{recipeId}}/approve`
- Auth: Bearer `adminToken`
- Expected: `200 OK`, recipe status becomes `APPROVED`.

## 25. Admin Delete Recipe

- Method: `DELETE`
- URL: `/admin/recipes/{{recipeId}}`
- Auth: Bearer `adminToken`
- Expected: `200 OK`.

## Other Useful Checks

- Swagger UI: `GET /swagger-ui.html`, expected `200 OK`.
- OpenAPI JSON: `GET /v3/api-docs`, expected `200 OK`.
- User routes without JWT: expected `403 Forbidden`.
- Admin routes with normal user JWT: expected `403 Forbidden`.
- Invalid recipe body, such as empty title: expected `400 Bad Request` with validation details.

## Repeatable Smoke Test

With the app running:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\api-smoke-test.ps1
```

Expected final line:

```text
ALL_ENDPOINT_SMOKE_TESTS_PASSED
```
