import React, { useState } from 'react'
import './assets/styles.css'

export default function CategoryNavigation({onCategoryClick}) {
    const categories = ['Fashions', "Electronics", "Mobiles", "TV & Appliances", "Furnitures","Home & Kitchen"];
    const images = [
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/ec2982e5564fe07c.png?q=100',
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/4d6b13d5a0e0724a.png?q=100',
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/cd6aca4f61e8ea95.png?q=100',
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/9c64dfa667885ca9.png?q=100',
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/cddd92e134ba3ea9.png?q=100',
        'https://rukminim2.flixcart.com/fk-p-flap/64/64/image/febcb9896245caf4.png?q=100'
        
    ]
    return (
        <div className='category-navigation'>
            <div className='category-list'>
                {categories.map((category, index) => (
                    <div key={index} className='category-item'>
                        <img src={images[index]} alt="" onClick={() => onCategoryClick(category)} />
                        <p onClick={() => onCategoryClick(category)}>{category}</p>
                    </div>
                ))}
            </div>
        </div>
    )
}
