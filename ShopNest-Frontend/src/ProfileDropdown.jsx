import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook
import useravatar from './useravatar.png';
import './assets/styles.css';
import Profile from './Profile';
export default function ProfileDropdown({ username, onChangeName }) {
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate(); // Initialize useNavigate hook
  const [showProfile, setShowProfile] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };
  const handleLogout = async () => {
    try {
      console.log("try logout")
      const response = await fetch('http://localhost:9090/api/auth/logout', {
        method: 'POST', // Use POST as logout often involves session clearing
        credentials: 'include', // Include credentials like cookies for authentication
      });

      if (response.ok) {
        console.log('User successfully logged out');
        navigate('/',{ replace: true }); // Redirect to login page
        window.location.reload();
      } else {
        console.error('Failed to log out');
      }
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };
  const handleOrdersClick = () => {
    navigate('/orders'); // Navigate to the orders route
  };

  
  
  return (
    <div className="profile-dropdown">
      <a className="profile-button" onClick={toggleDropdown}>
        <img
          src={useravatar}
          alt="User Avatar"
          className="user-avatar"
          onError={(e) => { e.target.src = 'fallback-logo.png'; }} // Fallback for image error
        />
        <span className="username">{username || 'Guest'}</span> {/* Display username */}
      </a>
      {isOpen && (
        <div className="dropdown-menu">
          <a onClick={() => setShowProfile(true)}>Profile</a>
          <a onClick={handleOrdersClick}>Orders</a> {/* Handle Orders Click */}
          <button className="profile-button" onClick={handleLogout}>
            Logout
          </button>
        </div>
      )}

      {showProfile && <Profile name={username} onChangeName={onChangeName} showProfile ={showProfile} setShowProfile = {setShowProfile} />}
    </div>
  );
}