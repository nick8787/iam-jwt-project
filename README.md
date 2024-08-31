# Business Logic for the Project

## Main Entities:
- **User**: User
- **Post**: Post
- **Comment**: Comment

## User Functions:

### Registration:
- Create a new user with a username and password.

### Login:
- Authenticate the user using username and password.

### Posts:
- **Create**: Users can create new posts.
- **Edit**: Users can edit only their own posts.
- **Delete**: Users can delete only their own posts.

### Comments:
- **Create**: Users can comment on any post.
- **Edit**: Users can edit only their own comments.
- **Delete**: Users can delete only their own comments.

## Interaction Between Entities:
- **User**:
    - **Post** (one to many): One user can create many posts.
    - **Comment** (one to many): One user can leave many comments.

- **Post**:
    - **Comment** (one to many): One post can contain many comments.

## Diagram:

- +-------------+
- | User |
- +-------------+
- |
- | 
- |
- V
- +-------------+
- | Post |
- +-------------+
- |
- | 
- |
- V
- +-------------+
- | Comment |
- +-------------+



- **User** registers and authenticates.
- **User** creates, edits, and deletes their own **Posts**.
- **User** writes, edits, and deletes their own **Comments**.
