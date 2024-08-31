# Business Logic for the Project

## Main Entities:
- **User**: Represents a user in the system.
- **Post**: Represents a post created by a user.
- **Comment**: Represents a comment left on a post.

## User Roles:
- **User**: A basic user with limited permissions.
- **Admin**: Has elevated privileges, including management of posts and comments.
- **Super Admin**: Has full control over the system, including user management.

## User Functions:

### Registration:
- Users can register by creating an account with a username and password.
- Users can create other users with a username and password.

### Login:
- Users can authenticate by providing a username and password.

### Posts:
- **Create**: Users can create new posts.
- **Edit**: Users can edit only their own posts. Admins can edit any post.
- **Delete**: Users can delete only their own posts. Admins can delete any post.

### Comments:
- **Create**: Users can comment on any post.
- **Edit**: Users can edit only their own comments. Admins can edit any comment.
- **Delete**: Users can delete only their own comments. Admins can delete any comment.

### Role-Specific Permissions:
- **User**:
  - Can create, edit, and delete their own posts and comments.
  - Can create new users.
  - Limited to managing their own content.
- **Admin**:
  - Can manage all posts and comments.
  - Has the ability to edit or delete any post or comment.
- **Super Admin**:
  - Has full control over all aspects of the system.
  - Can manage users, including their roles and permissions.

## Interaction Between Entities:
- **User**:
  - **Post** (one-to-many): One user can create many posts.
  - **Comment** (one-to-many): One user can leave many comments.
  - **User** (one-to-many): One user can create many other users.

- **Post**:
  - **Comment** (one-to-many): One post can contain many comments.

## Summary:
- **User** registers and authenticates.
- **User** creates, edits, and deletes their own **Posts**.
- **User** writes, edits, and deletes their own **Comments**.
- **User** can create new users.
- **Admins** and **Super Admins** have broader permissions, allowing them to manage content and users within the system.
