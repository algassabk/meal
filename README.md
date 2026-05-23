# Meal API

Meal API is a Spring Boot backend project for managing recipes, ingredients, categories, comments, favorites, meal plans, shopping lists, and users.

The project uses PostgreSQL as the database and JWT authentication for protected endpoints.

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Spring Data JPA / Hibernate
- Maven
- Docker
- Docker Compose
- MockMvc
- Swagger / OpenAPI

---

## Main Features

- Register and login
- JWT authentication
- Email verification
- Forgot password / password reset
- Change password
- Update user profile
- Upload profile picture
- Upload recipe image
- Recipe CRUD
- Category CRUD
- Ingredient CRUD
- Comments
- Favorites
- Meal plans
- Shopping lists
- Admin recipe approval
- Admin user deactivation
- Seed data
- Docker support
- API testing with MockMvc

## API Endpoints Table
| Method | Endpoint                                                              | Functionality                           | Access                             |
| ------ | --------------------------------------------------------------------- | --------------------------------------- | ---------------------------------- |
| POST   | `/auth/register`                                                      | Register a new user                     | Public                             |
| POST   | `/auth/login`                                                         | Login and receive JWT token             | Public                             |
| POST   | `/auth/email-verification/request`                                    | Request email verification token        | Public                             |
| GET    | `/auth/email-verification/verify?token=...`                           | Verify user email                       | Public                             |
| POST   | `/auth/password-reset/request`                                        | Request password reset token            | Public                             |
| POST   | `/auth/password-reset/confirm`                                        | Confirm password reset                  | Public                             |
| GET    | `/users/me`                                                           | Get current logged-in user              | Private                            |
| GET    | `/users/profile`                                                      | Get user profile                        | Private                            |
| PUT    | `/users/profile`                                                      | Update user profile                     | Private                            |
| POST   | `/users/profile-picture`                                              | Upload profile picture                  | Private                            |
| PUT    | `/users/change-password`                                              | Change user password                    | Private                            |
| POST   | `/categories`                                                         | Create category                         | Public / Admin depending on config |
| GET    | `/categories`                                                         | Get all categories                      | Public                             |
| GET    | `/categories/{id}`                                                    | Get category by ID                      | Public                             |
| PUT    | `/categories/{id}`                                                    | Update category                         | Public / Admin depending on config |
| DELETE | `/categories/{id}`                                                    | Delete category                         | Public / Admin depending on config |
| POST   | `/ingredients`                                                        | Create ingredient                       | Public / Admin depending on config |
| POST   | `/ingredients/bulk`                                                   | Create many ingredients                 | Public / Admin depending on config |
| GET    | `/ingredients`                                                        | Get all ingredients                     | Public                             |
| GET    | `/ingredients/{id}`                                                   | Get ingredient by ID                    | Public                             |
| PUT    | `/ingredients/{id}`                                                   | Update ingredient                       | Public / Admin depending on config |
| DELETE | `/ingredients/{id}`                                                   | Delete ingredient                       | Public / Admin depending on config |
| POST   | `/recipes`                                                            | Create recipe                           | Private                            |
| POST   | `/recipes/bulk`                                                       | Create many recipes                     | Private                            |
| GET    | `/recipes`                                                            | Get all recipes                         | Public                             |
| GET    | `/recipes/paged`                                                      | Get paged recipes                       | Public                             |
| GET    | `/recipes/{id}`                                                       | Get recipe by ID                        | Public                             |
| PUT    | `/recipes/{id}`                                                       | Update recipe                           | Private                            |
| DELETE | `/recipes/{id}`                                                       | Delete recipe                           | Private                            |
| GET    | `/recipes/search?keyword=...`                                         | Search recipes                          | Public                             |
| GET    | `/recipes/search/paged?keyword=...`                                   | Search recipes with pagination          | Public                             |
| GET    | `/recipes/category/{categoryId}`                                      | Get recipes by category                 | Public                             |
| GET    | `/recipes/category/{categoryId}/paged`                                | Get recipes by category with pagination | Public                             |
| PUT    | `/recipes/{id}/visibility?isPublic=true`                              | Update recipe visibility                | Private                            |
| POST   | `/recipes/{id}/image`                                                 | Upload recipe image                     | Private                            |
| POST   | `/recipes/{recipeId}/ingredients/{ingredientId}`                      | Add ingredient to recipe                | Private                            |
| GET    | `/recipes/{recipeId}/ingredients`                                     | Get recipe ingredients                  | Public                             |
| PUT    | `/recipes/{recipeId}/ingredients/{recipeIngredientId}/{ingredientId}` | Update recipe ingredient                | Private                            |
| DELETE | `/recipes/{recipeId}/ingredients/{recipeIngredientId}`                | Delete recipe ingredient                | Private                            |
| POST   | `/favorites/{recipeId}`                                               | Add recipe to favorites                 | Private                            |
| GET    | `/favorites`                                                          | Get user favorites                      | Private                            |
| DELETE | `/favorites/{favoriteId}`                                             | Remove favorite                         | Private                            |
| POST   | `/comments/recipe/{recipeId}`                                         | Add comment to recipe                   | Private                            |
| GET    | `/comments/recipe/{recipeId}`                                         | Get recipe comments                     | Public                             |
| PUT    | `/comments/{commentId}`                                               | Update comment                          | Private                            |
| DELETE | `/comments/{commentId}`                                               | Delete comment                          | Private                            |
| POST   | `/meal-plans`                                                         | Create meal plan                        | Private                            |
| GET    | `/meal-plans`                                                         | Get user meal plans                     | Private                            |
| GET    | `/meal-plans/{id}`                                                    | Get meal plan by ID                     | Private                            |
| PUT    | `/meal-plans/{id}`                                                    | Update meal plan                        | Private                            |
| DELETE | `/meal-plans/{id}`                                                    | Delete meal plan                        | Private                            |
| POST   | `/meal-plan-items/{mealPlanId}/{recipeId}`                            | Add recipe to meal plan                 | Private                            |
| GET    | `/meal-plan-items/{mealPlanId}`                                       | Get meal plan items                     | Private                            |
| DELETE | `/meal-plan-items/{mealPlanItemId}`                                   | Delete meal plan item                   | Private                            |
| POST   | `/shopping-lists`                                                     | Create shopping list                    | Private                            |
| GET    | `/shopping-lists`                                                     | Get user shopping lists                 | Private                            |
| GET    | `/shopping-lists/{id}`                                                | Get shopping list by ID                 | Private                            |
| PUT    | `/shopping-lists/{id}`                                                | Update shopping list                    | Private                            |
| DELETE | `/shopping-lists/{id}`                                                | Delete shopping list                    | Private                            |
| POST   | `/shopping-list-items/{shoppingListId}/{ingredientId}`                | Add item to shopping list               | Private                            |
| GET    | `/shopping-list-items/{shoppingListId}`                               | Get shopping list items                 | Private                            |
| PUT    | `/shopping-list-items/{shoppingListItemId}`                           | Update shopping list item               | Private                            |
| DELETE | `/shopping-list-items/{shoppingListItemId}`                           | Delete shopping list item               | Private                            |
| GET    | `/admin/recipes/pending`                                              | View pending recipes                    | Admin                              |
| PUT    | `/admin/recipes/{id}/approve`                                         | Approve recipe                          | Admin                              |
| DELETE | `/admin/recipes/{id}`                                                 | Delete recipe as admin                  | Admin                              |
| PUT    | `/admin/users/{id}/deactivate`                                        | Soft delete / deactivate user           | Admin                              |


---

## User Roles

### USER

A normal user can:

- Create recipes
- View recipes
- Add comments
- Save favorite recipes
- Create meal plans
- Create shopping lists
- Update profile
- Change password

### ADMIN

An admin can:

- View pending recipes
- Approve recipes
- Delete recipes
- Deactivate users

---

## Seed Data

When the project runs for the first time, it seeds basic data such as categories, ingredients, and an admin user.

Seeded admin account:

```text
Email: admin@meal.com
Password: admin123
