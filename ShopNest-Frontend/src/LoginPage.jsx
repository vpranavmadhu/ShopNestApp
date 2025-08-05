import React, { useState } from 'react'
import {useNavigate } from 'react-router-dom'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await fetch('http://localhost:9090/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ username, password })
      });

      const data = await response.json();
      if (response.ok) {
        console.log('User logged in successfully', data);
        if (data.role === 'CUSTOMER') {
          navigate('/customerhome', { replace: true });
        } else if (data.role === 'ADMIN') {
          navigate('/adminhome',  { replace: true });
        }
      } else {
        throw new Error(data.Error || 'Login failed');
      }

    } catch (error) {
      setError(error.message);
    }
  }

  return (
    <div className='page-container'>
      <div className='image-container'></div>
      <div className='form-container'>
        <h1 className='form-title'>LOGIN</h1>
        {error && <p className='error-message'>{error}</p>}
        <form action="" onSubmit={handleLogin} className='form-content'>
          <div className='form-group'>
            <label htmlFor="username" className='form-label'>Username</label>
            <input id='username' type="text" placeholder='Enter  your username' value={username} onChange={(e) => setUsername(e.target.value)} required className='form-input' />
          </div>
          <div>
            <label htmlFor="password" className='form-label'>Password</label>
            <input id='password' type="text" placeholder='Enter your password' value={password} onChange={(e) => setPassword(e.target.value)} required className='form-input' />
          </div>
          <button type='submit' className='form-button'>LOGIN</button>
        </form>
        <p className='form-footer'>
          New User?{' '}
          <a href="/register" className='form-link'>Sign Up here</a>
        </p>
      </div>
    </div>
  )
}
