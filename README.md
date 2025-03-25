# Tic-Tac-Toe Web Application

A simple web application to play Tic-Tac-Toe with a React frontend and Kotlin backend.

## Project Structure

- `src/main/kotlin/Main.kt`: Kotlin backend using a simple HTTP server
- `src/main/frontend/`: React frontend application

## Prerequisites

- JDK 8 or higher
- Node.js and npm
- Maven

## How to Run

### Backend

1. Build the project:
   ```
   mvn clean package
   ```

2. Run the backend:
   ```
   mvn exec:java
   ```

   The backend server will start on port 8080.

### Frontend

1. Navigate to the frontend directory:
   ```
   cd src/main/frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   npm start
   ```

   The frontend will be available at http://localhost:3000.

## How to Play

1. Open your browser and navigate to http://localhost:3000
2. The game will automatically create a new game session
3. Click on any empty square to make a move
4. The game will alternate between X and O players
5. The first player to get three in a row (horizontally, vertically, or diagonally) wins
6. If all squares are filled and no player has three in a row, the game ends in a draw
7. Click the "Reset Game" button to start a new game

## API Endpoints

- `POST /api/game/new`: Create a new game
- `POST /api/game/{gameId}/move`: Make a move in the game

## Technologies Used

- Backend:
  - Kotlin
  - Java HTTP Server

- Frontend:
  - React
  - CSS
  - Testing libraries (Jest, React Testing Library)
  - ESLint for code quality
  - Web Vitals for performance monitoring

## Dependencies

The project uses the following key dependencies:

- React 19.0.0
- React DOM 19.0.0
- React Scripts 5.0.1 (with automatic updates for minor versions)
- Web Vitals 4.2.4
- Testing libraries:
  - Jest DOM 6.6.3
  - React Testing Library 16.2.0
  - User Event 14.6.1
- ESLint 9.23.0 with React plugins

All dependencies use semantic versioning with the caret (^) notation to allow for automatic updates to minor versions, ensuring the project receives bug fixes and security patches while maintaining compatibility.

## Security

We take security seriously. For information about known vulnerabilities and our mitigation strategy, please see [Security Considerations](src/main/frontend/security.md).

## Future Improvements

- Add user authentication
- Add multiplayer support
- Add game history
- Add AI opponent
