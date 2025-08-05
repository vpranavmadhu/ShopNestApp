import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate hook
import useravatar from './useravatar.png';
import { CgProfile } from "react-icons/cg";
import { IoPersonSharp } from "react-icons/io5";
import { CiHeart } from "react-icons/ci";
import { RiCustomerService2Fill } from "react-icons/ri";
import { GoTag } from "react-icons/go";
import './assets/styles.css';
import Profile from './Profile';
import { FiLogOut } from "react-icons/fi";
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
      <div className="profile-button" onClick={toggleDropdown}>
        <IoPersonSharp />
        <span className="username">{username || 'Guest'}</span> {/* Display username */}
      </div>
      {isOpen && (
        <div className="dropdown-menu">
          <div className='dropdown-menuoptions'>
            <div onClick={() => setShowProfile(true)} className='option'><CgProfile /> MyProfile</div> 
            <div onClick={() => setShowProfile(true)} className='option'><CiHeart /> Wishlist</div>
            <div onClick={() => setShowProfile(true)} className='option'><GoTag /> Coupons</div>
            <div onClick={() => setShowProfile(true)} className='option'><RiCustomerService2Fill /> Service</div>
            <div onClick={handleLogout} className='option'>Logout <FiLogOut /></div>
          </div>
          
        </div>
      )}

      {showProfile && <Profile name={username} onChangeName={onChangeName} showProfile ={showProfile} setShowProfile = {setShowProfile} />}
    </div>
  );
}