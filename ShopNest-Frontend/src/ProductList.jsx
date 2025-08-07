import React from 'react'
import { FaRegHeart } from "react-icons/fa";
import { FaShoppingCart } from "react-icons/fa";
export default function ProductList({products, onAddToCart}) {
    if(products.length === 0) {
        return <div className='no-product'> 
          <p>No products available.</p>
          </div>
    }
  return (
    <div className='product-grid'>
      {products.map((product) => (
        <div key={product.product_id} className='product-card'>
            <img src={product.images[0]} alt={product.name} loading='lazy' onError={(e) => {
                
                // e.target.src = 'https://via.placeholder.com/150';
                e.target.src = 'https://picsum.photos/150';
            }} />
            <h3>{product.name}</h3>
            <p>{product.description}</p>
            <div className='product-price'>
              <h3>₹{product.price} </h3>
              <p>₹{(product.price / (1- .15)).toFixed(2)}</p>
              <p>15% OFF</p>
            </div>
            <div className='product-actions'>
              <button className='product-actions-addtocart' onClick={() => onAddToCart(product.product_id)}><FaShoppingCart /> Add To Cart</button>
              <button className='product-actions-wishlist'><FaRegHeart /></button>
            </div>
            
        </div>    
      ))}
    </div>
  )
}
