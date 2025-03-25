import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [board, setBoard] = useState(Array(9).fill(null));
  const [isXNext, setIsXNext] = useState(true);
  const [status, setStatus] = useState('Next player: X');
  const [gameId, setGameId] = useState(null);

  useEffect(() => {
    // Create a new game when the component mounts
    fetch('http://localhost:8080/api/game/new', {
      method: 'POST',
    })
      .then(response => response.json())
      .then(data => {
        setGameId(data.id);
        setBoard(Array(9).fill(null));
        setIsXNext(true);
        setStatus('Next player: X');
      })
      .catch(error => {
        console.error('Error creating new game:', error);
        setStatus('Error creating game. Please try again.');
      });
  }, []);

  const calculateWinner = (squares) => {
    const lines = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
      const [a, b, c] = lines[i];
      if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
        return squares[a];
      }
    }
    return null;
  };

  const handleClick = (i) => {
    if (!gameId) return;
    
    const boardCopy = [...board];
    if (calculateWinner(boardCopy) || boardCopy[i]) {
      return;
    }
    
    // Make a move on the backend
    fetch(`http://localhost:8080/api/game/${gameId}/move`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        position: i,
        player: isXNext ? 'X' : 'O',
      }),
    })
      .then(response => response.json())
      .then(data => {
        // Update the board with the new state from the server
        setBoard(data.board);
        setIsXNext(!isXNext);
        
        const winner = calculateWinner(data.board);
        if (winner) {
          setStatus(`Winner: ${winner}`);
        } else if (data.board.every(square => square !== null)) {
          setStatus('Game ended in a draw');
        } else {
          setStatus(`Next player: ${!isXNext ? 'X' : 'O'}`);
        }
      })
      .catch(error => {
        console.error('Error making move:', error);
        setStatus('Error making move. Please try again.');
      });
  };

  const renderSquare = (i) => {
    return (
      <button className="square" onClick={() => handleClick(i)}>
        {board[i]}
      </button>
    );
  };

  const resetGame = () => {
    // Create a new game
    fetch('http://localhost:8080/api/game/new', {
      method: 'POST',
    })
      .then(response => response.json())
      .then(data => {
        setGameId(data.id);
        setBoard(Array(9).fill(null));
        setIsXNext(true);
        setStatus('Next player: X');
      })
      .catch(error => {
        console.error('Error creating new game:', error);
        setStatus('Error creating game. Please try again.');
      });
  };

  return (
    <div className="game">
      <div className="game-board">
        <div className="status">{status}</div>
        <div className="board-row">
          {renderSquare(0)}
          {renderSquare(1)}
          {renderSquare(2)}
        </div>
        <div className="board-row">
          {renderSquare(3)}
          {renderSquare(4)}
          {renderSquare(5)}
        </div>
        <div className="board-row">
          {renderSquare(6)}
          {renderSquare(7)}
          {renderSquare(8)}
        </div>
        <button className="reset-button" onClick={resetGame}>
          Reset Game
        </button>
      </div>
    </div>
  );
}

export default App;