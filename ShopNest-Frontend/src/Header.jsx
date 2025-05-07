import React from 'react'
import Logo from './Logo'
import CartIcon from './CartIcon'
import ProfileDropdown from './ProfileDropdown'
import './assets/styles.css'

export default function Header({cartCount, username, onChangeName}) {
  return (
    <header className='header'>
        <div className='header-content'>
            <Logo />
            <div className='header-actions'>
                <CartIcon count = {cartCount} />
                <ProfileDropdown username={username} onChangeName={onChangeName} />
            </div>
        </div>

    </header>
  )
}
