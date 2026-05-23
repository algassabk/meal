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
