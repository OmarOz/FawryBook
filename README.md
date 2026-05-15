# 📖 FawryBook — Technical Blogging Platform
 
FawryBook is a full-stack technical blogging platform where developers can share knowledge, interact with posts through likes/dislikes and comments, and manage their professional presence.
 
---
 
## 📋 Table of Contents
 
- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Setup Instructions](#setup-instructions)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
---
 
## 🛠 Tech Stack
 
| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.x |
| Frontend | Angular 19, Tailwind CSS |
| Database | PostgreSQL 17 (Docker) |
| Authentication | JWT (JSON Web Tokens) |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct |
| Build Tool | Maven |
 
---
 
## 🏗 Architecture Overview
 
FawryBook follows a layered architecture on the backend and a component-based architecture on the frontend.
 
### Backend Architecture
 
```
Request → SecurityFilter (JWT) → Controller → Service → Repository → Database
```
 
**Layers:**
 
- **Controllers** — Handle HTTP requests and responses, delegate to services
- **Services** — Business logic layer (interfaces + implementations)
- **Repositories** — Data access layer using Spring Data JPA
- **Entities** — JPA-mapped database models
- **DTOs** — Data Transfer Objects to control what's exposed in the API
- **Mappers** — MapStruct mappers to convert between entities and DTOs
- **Security** — JWT filter, UserDetailsService, SecurityConfig
### Frontend Architecture
 
```
User → Angular Component → Service (HTTP) → Spring Boot API → Database
```
 
**Layers:**
 
- **Pages** — Full page components (auth, home, post detail, profile, etc.)
- **Shared Components** — Reusable UI components (navbar, post card, etc.)
- **Services** — HTTP client wrappers for each API domain
- **Models** — TypeScript interfaces matching backend DTOs
- **Guards** — Route protection for authenticated pages
- **Interceptors** — Automatically attaches JWT token to every request
---
 
## ⚙️ Setup Instructions
 
### Prerequisites
 
- **Java 17+** — [Download](https://adoptium.net/)
- **Maven** — [Download](https://maven.apache.org/download.cgi)
- **Docker Desktop** — [Download](https://www.docker.com/products/docker-desktop/) — must be running
- **Node.js 18+** — [Download](https://nodejs.org/)
- **Angular CLI** — Install with `npm install -g @angular/cli`
---
 
### Backend Setup
 
#### 1. Start Docker Desktop
Make sure Docker Desktop is running before proceeding.
 
#### 2. Start the PostgreSQL Database
 
Navigate to the backend root folder and run:
 
```bash
docker-compose up -d
```
 
This starts:
- **PostgreSQL** on port `5432` — the main database
- **Adminer** on port `8888` — optional database management UI
> You can access Adminer at `http://localhost:8888`  
> Server: `db`, Username: `postgres`, Password: `1234`, Database: `fawrybook`
 
#### 3. Configure Application Properties
 
The backend is pre-configured. The `application.properties` connects to the Docker database automatically. No changes needed.
 
#### 4. Build and Run the Backend
 
```bash
# Clean and install dependencies
mvn clean install -DskipTests
 
# Run the application
mvn spring-boot:run
```
 
Or in IntelliJ IDEA: **Run → FawrybookApplication**
 
The backend starts on **`http://localhost:8080`**
 
> On first run, Hibernate automatically creates all database tables via `ddl-auto=update`.
 
---
 
### Frontend Setup
 
#### 1. Navigate to the Frontend Folder
 
```bash
cd fawrybook-frontend
```
 
#### 2. Install Dependencies
 
```bash
npm install
```
 
#### 3. Start the Development Server
 
```bash
npm start
```
 
The frontend starts on **`http://localhost:4200`**
 
> The API base URL is configured in `src/environments/environment.ts`
 
---
 
### Running Both Together
 
| Service | URL |
|---------|-----|
| Backend API | http://localhost:8080 |
| Frontend App | http://localhost:4200 |
| Adminer (DB UI) | http://localhost:8888 |
 
---
 
## 📁 Project Structure
 
### Backend
 
```
Backend/Fawrybook/src/main/java/org/blog/fawrybook/
├── component/              # Shared components (e.g. token blacklist)
├── config/
│   └── SecurityConfig.java # JWT security configuration, CORS
├── controllers/            # REST controllers (HTTP layer)
├── domain/
│   ├── dtos/               # Data Transfer Objects (request/response)
│   ├── entities/           # JPA entities (User, Post, Comment, etc.)
│   ├── CreatePostRequest.java
│   └── UpdatePostRequest.java
├── enums/                  # Enums (InteractionType: LIKE/DISLIKE)
├── mappers/                # MapStruct mappers (Entity ↔ DTO)
├── repositories/           # Spring Data JPA repositories
├── security/               # JWT filter, BlogUserDetails, BlogUserDetailsService
├── services/
│   ├── AuthenticationService.java
│   ├── CategoryService.java
│   ├── PostService.java
│   ├── TagService.java
│   ├── UserService.java
│   └── implmentation/      # Service implementations
└── FawrybookApplication.java
```
 
### Frontend
 
```
fawrybook-frontend/src/app/
├── core/
│   ├── guards/             # AuthGuard — protects authenticated routes
│   └── interceptors/       # AuthInterceptor — attaches JWT to requests
├── models/                 # TypeScript interfaces (Post, User, Comment, etc.)
├── pages/
│   ├── auth/               # Login and Register pages
│   ├── create-post/        # Create new post page
│   ├── edit-post/          # Edit existing post page
│   ├── post-details/       # Single post detail page with comments
│   ├── posts/              # Home feed showing all posts
│   └── profile/            # User profile view and edit page
├── services/               # HTTP services (AuthService, PostService, etc.)
└── shared/components/
    ├── navbar/             # Navigation bar component
    ├── post-card/          # Post summary card component
    ├── post-details-component/  # Post detail display component
    └── post-form/          # Reusable post form component
```
 
---
 
## 📡 API Documentation
 
**Base URL:** `http://localhost:8080/api/v1`
 
**Authentication:** Protected endpoints require a Bearer token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```
 
> 💡 **Postman Tip:** The Register and Login requests automatically save the JWT token to the `jwt_token` environment variable via a test script. All other requests use `{{jwt_token}}` automatically.
 
---
 
### 🔐 Authentication
 
#### Register
```
POST /auth/register
```
**Body:**
```json
{
  "email": "john@example.com",
  "username": "john",
  "password": "secret_password"
}
```
**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400
}
```
 
---
 
#### Login
```
POST /auth/login
```
**Body:**
```json
{
  "email": "john@example.com",
  "password": "secret_password"
}
```
**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 86400
}
```
 
---
 
#### Logout
```
POST /auth/logout
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Response:** `200 OK`
```json
"Logged out successfully"
```
 
---
 
### 👤 User Profile
 
#### Get My Profile
```
GET /users/me
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Response:** `200 OK`
```json
{
  "id": "970befb0-bbe5-423a-bbf1-908c8a8c104b",
  "username": "john",
  "email": "john@example.com",
  "createdAt": "2026-05-13T00:20:36"
}
```
 
---
 
#### Update My Profile
```
PUT /users/me
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{
  "username": "new_username"
}
```
**Response:** `200 OK` — updated profile object
 
---
 
### 📝 Posts
 
#### Get All Posts
```
GET /posts/all
```
**Auth:** Not required
 
**Response:** `200 OK` — array of post objects
```json
[
  {
    "id": "uuid",
    "title": "Post Title",
    "content": "Post content...",
    "author": { "id": "uuid", "username": "john" },
    "category": { "id": "uuid", "name": "Backend", "postCount": 5 },
    "tags": [],
    "createdAt": "2026-05-13T00:20:36",
    "updatedAt": "2026-05-13T00:20:36",
    "likesCount": 3,
    "dislikesCount": 1,
    "commentsCount": 2,
    "comments": [],
    "rating": 75.0
  }
]
```
 
---
 
#### Get Post by ID
```
GET /posts/{id}
```
**Auth:** Not required
 
**Response:** `200 OK` — single post object with full comments
 
---
 
#### Create Post
```
POST /posts/create
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{
  "title": "My Post Title",
  "content": "Post content here...",
  "categoryId": "uuid-of-category",
  "tagIds": ["uuid-of-tag-1", "uuid-of-tag-2"]
}
```
**Response:** `201 Created` — created post object
 
---
 
#### Update Post
```
PUT /posts/{id}
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
> Only the post author can update their post. Returns `403 Forbidden` otherwise.
 
**Body:**
```json
{
  "title": "Updated Title",
  "content": "Updated content...",
  "categoryId": "uuid-of-category",
  "tagIds": ["uuid-of-tag"]
}
```
**Response:** `200 OK` — updated post object
 
---
 
#### Delete Post
```
DELETE /posts/{id}
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
> Only the post author can delete their post. Returns `403 Forbidden` otherwise.
 
**Response:** `200 OK`
 
---
 
### 💬 Comments
 
#### Add Comment
```
POST /posts/comment
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{
  "postId": "uuid-of-post",
  "content": "This is my comment"
}
```
**Response:** `200 OK` — comment object
 
---
 
### 👍 Interactions (Like / Dislike)
 
#### Interact with Post
```
POST /posts/interaction
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{
  "postId": "uuid-of-post",
  "interactionType": "LIKE"
}
```
 
> `interactionType` accepts: `LIKE` or `DISLIKE`
>
> **Toggle behavior:**
> - First interaction → creates it
> - Same interaction again → removes it (toggle off)
> - Different interaction → switches from LIKE to DISLIKE or vice versa
 
**Response:** `200 OK` — updated post object with new counts
 
---
 
### 🏷️ Categories
 
#### Get All Categories
```
GET /categories
```
**Auth:** Not required
 
**Response:** `200 OK`
```json
[
  { "id": "uuid", "name": "Backend", "postCount": 5 },
  { "id": "uuid", "name": "Frontend", "postCount": 3 }
]
```
 
---
 
#### Create Category
```
POST /categories
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{ "name": "DevOps" }
```
**Response:** `200 OK` — created category object
 
---
 
#### Delete Category
```
DELETE /categories/{id}
```
**Response:** `200 OK`
 
---
 
### 🔖 Tags
 
#### Get All Tags
```
GET /tags
```
**Auth:** Not required
 
**Response:** `200 OK` — array of tag objects
 
---
 
#### Create Tags
```
POST /tags
```
**Headers:** `Authorization: Bearer {{jwt_token}}`
 
**Body:**
```json
{
  "names": ["java", "spring", "backend"]
}
```
**Response:** `200 OK` — array of created tag objects
 
---
 
## 🗄️ Database Schema
 
The database is automatically created by Hibernate on first startup. The schema consists of 7 tables:
 
```
users
  └── id, email, password, username, created_at, updated_at
 
categories
  └── id, name
 
tag
  └── id, name
 
posts
  └── id, title, content, author_id (→users), category_id (→categories), created_at, updated_at
 
post_tags  [junction table]
  └── post_id (→posts), tag_id (→tag)
 
comments
  └── id, content, post_id (→posts), user_id (→users), created_at, updated_at
 
post_interactions
  └── id, type (LIKE/DISLIKE), post_id (→posts), user_id (→users), created_at, updated_at
      UNIQUE constraint on (post_id, user_id)
```
 
See `database/schema.sql` for the complete SQL schema.
 
---
 
## 🔑 Key Design Decisions
 
- **JWT Stateless Auth** — No server-side sessions. Tokens expire after 24 hours. Logout is handled via a token blacklist stored in memory.
- **DTO Pattern** — Entities are never exposed directly in API responses. DTOs control exactly what data is sent to the client, preventing circular references and hiding sensitive fields like passwords.
- **Ownership Enforcement** — Post edit and delete operations verify the requesting user is the post author before proceeding.
- **Rating Calculation** — Post rating is calculated dynamically as `(likes / total interactions) * 100`, not stored in the database.
- **Lazy Loading with JOIN FETCH** — JPQL queries use `JOIN FETCH` to avoid N+1 query problems when loading posts with their authors, categories, and tags.
