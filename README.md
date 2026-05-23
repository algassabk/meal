# Meal API

Recipe and Meal Prep Social Platform backend built with Spring Boot. The API supports users, recipes, ingredients, favorites, comments, meal plans, shopping lists, admin recipe approval, image upload, email verification tokens, password reset tokens, and Swagger documentation.

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Security
- JWT authentication
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Spring Validation
- Springdoc OpenAPI / Swagger UI
- Docker and Docker Compose

## Features

- Register and login with JWT
- Logged-in user change password
- Current logged-in user profile
- Categories CRUD
- Ingredients CRUD
- Recipes CRUD
- Public recipe reads and protected recipe writes
- Recipe search, category filter, pagination, and sorting
- Recipe ingredients
- Favorites
- Comments
- Meal plans and meal plan items
- Shopping lists and shopping list items
- Admin recipe pending list, approval, recipe delete, and user deactivate
- Recipe image upload with static `/uploads/**` access
- Email verification token flow
- Password reset token flow
- Global validation and runtime exception responses
- Swagger/OpenAPI documentation
- Spring profiles for local development and Docker
- First-run seed data for categories, ingredients, and an admin user

## Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE meal_db;
```

Common settings are in `src/main/resources/application.properties`.

Local development database settings are in `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/meal_db
spring.datasource.username=postgres
spring.datasource.password=12345678
```

Docker database settings are in `src/main/resources/application-docker.properties`. Docker Compose sets:

```properties
SPRING_PROFILES_ACTIVE=docker
```

The app uses the `dev` profile by default:

```properties
spring.profiles.active=dev
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

Update these values if your local PostgreSQL username or password is different.

## Seed Data

On first run, the app seeds:

- Categories: Breakfast, Lunch, Dinner
- Ingredients: Rice, Chicken, Tomato
- Admin user:
  - Email: `admin@meal.com`
  - Password: `admin123`

This is for local capstone testing only.

## Run Locally

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Test

Run unit/context tests:

```powershell
.\mvnw.cmd test
```

With the app running, run the full API smoke test:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\api-smoke-test.ps1
```

The smoke test registers users, creates data, tests protected/admin routes, verifies email/password token flows, uploads an image, checks Swagger, and deletes created records.

## Docker

Check the Docker Compose file:

```powershell
docker compose config --quiet
```

Run PostgreSQL and the app:

```powershell
docker compose up --build
```

Stop containers:

```powershell
docker compose down
```

Uploaded images are stored in the local `uploads/` folder and mounted into the app container.

## Main Endpoints

Auth:

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/email-verification/request`
- `GET /auth/email-verification/verify?token=...`
- `POST /auth/password-reset/request`
- `POST /auth/password-reset/confirm`

Users:

- `GET /users/me`
- `GET /users/profile`
- `PUT /users/change-password`

Categories:

- `POST /categories`
- `GET /categories`
- `GET /categories/{id}`
- `PUT /categories/{id}`
- `DELETE /categories/{id}`

Recipes:

- `POST /recipes`
- `GET /recipes`
- `GET /recipes/paged`
- `GET /recipes/{id}`
- `PUT /recipes/{id}`
- `DELETE /recipes/{id}`
- `GET /recipes/search?keyword=...`
- `GET /recipes/search/paged?keyword=...`
- `GET /recipes/category/{categoryId}`
- `GET /recipes/category/{categoryId}/paged`
- `PUT /recipes/{id}/visibility?isPublic=true`
- `POST /recipes/{id}/image`

Ingredients:

- `POST /ingredients`
- `GET /ingredients`
- `GET /ingredients/{id}`
- `PUT /ingredients/{id}`
- `DELETE /ingredients/{id}`

Recipe ingredients:

- `POST /recipes/{recipeId}/ingredients/{ingredientId}`
- `GET /recipes/{recipeId}/ingredients`
- `PUT /recipes/{recipeId}/ingredients/{recipeIngredientId}/{ingredientId}`
- `DELETE /recipes/{recipeId}/ingredients/{recipeIngredientId}`

Favorites:

- `POST /favorites/{recipeId}`
- `GET /favorites`
- `DELETE /favorites/{favoriteId}`

Comments:

- `POST /comments/recipe/{recipeId}`
- `GET /comments/recipe/{recipeId}`
- `PUT /comments/{commentId}`
- `DELETE /comments/{commentId}`

Meal plans:

- `POST /meal-plans`
- `GET /meal-plans`
- `GET /meal-plans/{id}`
- `PUT /meal-plans/{id}`
- `DELETE /meal-plans/{id}`

Meal plan items:

- `POST /meal-plan-items/{mealPlanId}/{recipeId}`
- `GET /meal-plan-items/{mealPlanId}`
- `DELETE /meal-plan-items/{mealPlanItemId}`

Shopping lists:

- `POST /shopping-lists`
- `GET /shopping-lists`
- `GET /shopping-lists/{id}`
- `PUT /shopping-lists/{id}`
- `DELETE /shopping-lists/{id}`

Shopping list items:

- `POST /shopping-list-items/{shoppingListId}/{ingredientId}`
- `GET /shopping-list-items/{shoppingListId}`
- `PUT /shopping-list-items/{shoppingListItemId}`
- `DELETE /shopping-list-items/{shoppingListItemId}`

Admin:

- `GET /admin/recipes/pending`
- `PUT /admin/recipes/{id}/approve`
- `DELETE /admin/recipes/{id}`
- `PUT /admin/users/{id}/deactivate`

## Auth Notes

- Public: auth routes, category routes, ingredient routes, recipe GET routes, comment GET routes, uploaded files, Swagger/OpenAPI.
- Requires JWT: user routes, recipe write routes, favorites, comment writes, meal plans, meal plan items, shopping lists, shopping list items.
- Requires ADMIN role: `/admin/**`.

For local admin testing, register a user and update the database role:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@example.com';
```

You can also use the seeded admin user:

```text
admin@meal.com / admin123
```
