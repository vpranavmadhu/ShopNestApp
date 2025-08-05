import React, { useState } from 'react'
import Logo from './Logo'
import CartIcon from './CartIcon'
import ProfileDropdown from './ProfileDropdown'
import './assets/styles.css'
import { BsFillBoxFill } from "react-icons/bs";
import { useNavigate } from 'react-router-dom'
import { FaSearch } from "react-icons/fa";

export default function Header({cartCount, username, onChangeName,setSearch,search}) {
  const navigate = useNavigate()
  return (
    <header className='header'>
        <div className='header-content'>
            <Logo />
            <div className='header-search'>
                  <label htmlFor=""><FaSearch /></label>
                  <input type="text" placeholder='Search products, brands and more' value={search} onChange={(e)=> setSearch(e.target.value)} /> 
                </div>
            <div className='header-actions'>
                
                <div className='order-icon' onClick={()=> navigate('/orders')}>
                  <BsFillBoxFill />
                </div>
                <CartIcon count = {cartCount} />
                <ProfileDropdown username={username} onChangeName={onChangeName} />

                
            </div>
        </div>

    </header>
  )
}
