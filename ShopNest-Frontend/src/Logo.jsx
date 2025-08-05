import React from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import logo from './logo.png'

export default function Logo() {
    const navigate = useNavigate();
    const location = useLocation();

    const handleClick = () => {
    if (location.pathname === '/admindashboard') return; // disable click on /admin
    navigate('/customerhome');
    }
  return (
    <div className='logo-container' onClick={handleClick}>
      <img src="/shopnest.png" alt="SalesSavvy Logo" className='logo-image' onError={(e) => {e.target.src = 'fallback-logo.png'}} />
      <span className='logo-text'>ShopNest</span>
      <h3>super saver</h3>
    </div>
  )
}
