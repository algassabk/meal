````markdown
# Meal API

Recipe and Meal Prep Social Platform backend built with Spring Boot.  
The API supports users, recipes, ingredients, favorites, comments, meal plans, shopping lists, admin recipe approval, image upload, email verification, password reset, Swagger documentation, pagination, and Docker support.

---

# Tech Stack

- Java 17
- Spring Boot 4
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Spring Validation
- Springdoc OpenAPI / Swagger UI
- Docker & Docker Compose

---

# Features

## Authentication & Security
- Register and login with JWT
- Current logged-in user profile
- Protected routes with Spring Security
- Role-based admin access

## Recipes
- Categories CRUD
- Ingredients CRUD
- Recipes CRUD
- Public recipe reads and protected recipe writes
- Recipe visibility (public/private)
- Recipe search
- Recipe category filter
- Recipe pagination and sorting
- Recipe image upload

## Recipe Interactions
- Recipe ingredients
- Favorites
- Comments

## Meal Planning
- Meal plans
- Meal plan items

## Shopping Lists
- Shopping lists
- Shopping list items

## Admin Features
- Pending recipe list
- Recipe approval
- Admin recipe delete

## Additional Features
- Email verification token flow
- Password reset token flow
- Global validation handling
- Global exception handling
- Swagger/OpenAPI documentation
- Docker support

---

# Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE meal_db;
````

Default database settings are located in:

```text
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/meal_db
spring.datasource.username=postgres
spring.datasource.password=12345678

spring.jpa.hibernate.ddl-auto=update

server.port=8080
```

Update these values if your PostgreSQL username or password is different.

---

# Run Locally

Start the application:

```bash
.\mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

# Testing

Run unit/context tests:

```bash
.\mvnw.cmd test
```

With the app running, execute the smoke test:

```bash
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\api-smoke-test.ps1
```

The smoke test verifies:

* Authentication flow
* Protected/admin routes
* CRUD operations
* Email verification
* Password reset
* Image upload
* Pagination/search/filter
* Swagger access

---

# Docker

Validate the Docker Compose configuration:

```bash
docker compose config --quiet
```

Run PostgreSQL and the application:

```bash
docker compose up --build
```

Stop containers:

```bash
docker compose down
```

Uploaded images are stored in the local:

```text
uploads/
```

folder and mounted into the application container.

---

# Main API Endpoints

## Authentication

```http
POST /auth/register
POST /auth/login

POST /auth/email-verification/request
GET  /auth/email-verification/verify?token=...

POST /auth/password-reset/request
POST /auth/password-reset/confirm
```

---

## Users

```http
GET /users/me
GET /users/profile
```

---

## Categories

```http
POST   /categories
GET    /categories
GET    /categories/{id}
PUT    /categories/{id}
DELETE /categories/{id}
```

---

## Recipes

```http
POST   /recipes
GET    /recipes
GET    /recipes/paged
GET    /recipes/{id}
PUT    /recipes/{id}
DELETE /recipes/{id}

GET    /recipes/search?keyword=...
GET    /recipes/search/paged?keyword=...

GET    /recipes/category/{categoryId}
GET    /recipes/category/{categoryId}/paged

PUT    /recipes/{id}/visibility?isPublic=true

POST   /recipes/{id}/image
```

---

## Ingredients

```http
POST   /ingredients
GET    /ingredients
GET    /ingredients/{id}
PUT    /ingredients/{id}
DELETE /ingredients/{id}
```

---

## Recipe Ingredients

```http
POST   /recipes/{recipeId}/ingredients/{ingredientId}
GET    /recipes/{recipeId}/ingredients
PUT    /recipes/{recipeId}/ingredients/{recipeIngredientId}/{ingredientId}
DELETE /recipes/{recipeId}/ingredients/{recipeIngredientId}
```

---

## Favorites

```http
POST   /favorites/{recipeId}
GET    /favorites
DELETE /favorites/{favoriteId}
```

---

## Comments

```http
POST   /comments/recipe/{recipeId}
GET    /comments/recipe/{recipeId}
PUT    /comments/{commentId}
DELETE /comments/{commentId}
```

---

## Meal Plans

```http
POST   /meal-plans
GET    /meal-plans
GET    /meal-plans/{id}
PUT    /meal-plans/{id}
DELETE /meal-plans/{id}
```

---

## Meal Plan Items

```http
POST   /meal-plan-items/{mealPlanId}/{recipeId}
GET    /meal-plan-items/{mealPlanId}
DELETE /meal-plan-items/{mealPlanItemId}
```

---

## Shopping Lists

```http
POST   /shopping-lists
GET    /shopping-lists
GET    /shopping-lists/{id}
PUT    /shopping-lists/{id}
DELETE /shopping-lists/{id}
```

---

## Shopping List Items

```http
POST   /shopping-list-items/{shoppingListId}/{ingredientId}
GET    /shopping-list-items/{shoppingListId}
PUT    /shopping-list-items/{shoppingListItemId}
DELETE /shopping-list-items/{shoppingListItemId}
```

---

## Admin

```http
GET    /admin/recipes/pending
PUT    /admin/recipes/{id}/approve
DELETE /admin/recipes/{id}
```

---

# Authentication Notes

## Public Routes

* Authentication routes
* Category routes
* Ingredient routes
* Recipe GET routes
* Comment GET routes
* Uploaded files
* Swagger/OpenAPI routes

## JWT Protected Routes

* User routes
* Recipe write routes
* Favorites
* Comment write routes
* Meal plans
* Meal plan items
* Shopping lists
* Shopping list items

## Admin Protected Routes

```text
/admin/**
```

requires:

```text
ROLE_ADMIN
```

---

# Admin Testing

For local admin testing:

1. Register a normal user
2. Update the database role manually:

```sql
UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin@example.com';
```

---

# Postman Collection

A ready-to-use Postman collection is included:

```text
Meal_API.postman_collection.json
```

Import it into Postman to test all endpoints.

---

# API Testing Guide

Detailed endpoint testing instructions are available in:

```text
API_TESTING_GUIDE.md
```

---

# Project Status

Backend implementation completed and stabilized with:

* endpoint testing
* Swagger documentation
* Docker support
* validation handling
* pagination
* image upload
* authentication flows
* admin functionality

```
```
