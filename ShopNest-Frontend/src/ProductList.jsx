import React from 'react'

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
            <h3>₹{product.price} </h3>
            <button onClick={() => onAddToCart(product.product_id)}>Add to Cart </button>
        </div>    
      ))}
    </div>
  )
}
