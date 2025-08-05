import React from 'react'
import { useNavigate } from 'react-router-dom'
import './assets/styles.css'
import { FaShoppingCart } from "react-icons/fa";

export default function CartIcon({count}) {
    const navigate = useNavigate();

    const handleCartClick = () => {
        navigate('/UserCartPage');
    };
  return (
    <div className='cart-icon' onClick={handleCartClick}>
        
        < FaShoppingCart />
        <span className='cart-badge'>{count}</span> 
      
    </div>
  )
}
