import React, { useEffect, useState } from 'react'
import './assets/profileStyle.css'

export default function Profile({ showProfile, setShowProfile, name , onChangeName}) {

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [isEditable, setIsEditable] = useState(false);


  useEffect(() => {
    fetchUserDetails();
  },[]
  )

  const fetchUserDetails = async () => {

    try {
      const response = await fetch('http://localhost:9090/api/user/profile', {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      }
      );
      if (response.ok) {
        const data = await response.json();

        if (data) {
          setUsername(data.name);
          setEmail(data.email);
        } else {
          console.log("empty user")
        }

      }
    } catch (error) {
      console.error(error);

    }
  }

  const firstLetter = username ? username.charAt(0).toUpperCase() : "?";

  const handleBack = () => {
    setShowProfile(false);

  }

  const handleSave = async () => {
    setIsEditable(false);
    console.log(username);
    console.log(email);

    try {
      const response = await fetch('http://localhost:9090/api/user/update', {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username, email }),
      });

      const data = await response.json();

      if (response.ok) {
        console.log('Updated user successful', data);
        setUsername(data.username);
        setEmail(data.email);
        onChangeName(data.username);

      } else {
        throw new Error(data.error || "Modification failed");
      }
    } catch (error){
      console.log(error);
    }
    

  }

  return (
    <div className='profile-main'>
      <div className='profile-card'>
        <div className='left-profile-card'>
          <div className='profile'>{firstLetter}</div>
        </div>
        <div className='right-profile-card'>
          <h2>Personal Information</h2>
          <label htmlFor="">Name: <input type="text"  value={username} disabled={!isEditable} onChange={(e) => { setUsername(e.target.value) }} required /></label>
          <label htmlFor="">Email: <input type="text"  value={email} disabled={!isEditable} onChange={(e) => { setEmail(e.target.value) }} required /></label>

          <div className='profile-actions'>
            <button onClick={() => setIsEditable(true)}>edit</button>
            {isEditable && <button onClick={handleSave}>save</button>}
          </div>
        </div>
      </div>
      <div>
        <button onClick={handleBack} className='back-button'>BACK</button>
      </div>
    </div>
  )

  
}
