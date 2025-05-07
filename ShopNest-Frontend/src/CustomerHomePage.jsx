import React, { useEffect, useState } from 'react'
import Header from './Header';
import CategoryNavigation from './CategoryNavigation';
import './assets/styles.css'
import ProductList from './ProductList';
import Footer from './Footer';
import { useNavigate } from 'react-router-dom';

export default function CustomerHomePage() {
  const [products, setProducts] = useState([]);
  const [cartCount, setCartCount] = useState(0);
  const [username, setUsername] = useState('');
  const [cartError, setCartError] = useState(false);
  const [isCartLoading, setIsCartLoading] = useState(true);

  

  useEffect(() => {
    fetchProducts();
    if (username) {
      fetchCartCount(); // fetch cart count only is username available
    }
  }, [username]); // Re-run cart count only if username changes

  const fetchProducts = async (category = '') => {
    try {
      const response = await fetch(`http://localhost:9090/api/products${category ? `?category=${category}` : `?category=Shirts`}`,
        { credentials: 'include' }
      )
      const data = await response.json();
      console.log(data)
      if (data) {
        setUsername(data.user?.name || 'Guest');
        setProducts(data.products || []);
      } else {
        setProducts([]);
      }
    } catch (error) {
      console.error('Error fetching products:', error);
      setProducts([]);
    }
  };

  const fetchCartCount = async () => {
    setIsCartLoading(true); // set loading state
    try {
      const response = await fetch(`http://localhost:9090/api/cart/items/count?username=${username}`, {
        credentials: 'include', // Include authtoken as  a cookie
      });
      const count = await response.json();
      setCartCount(count);
      setCartError(false);  // Reset error state if successful
    } catch (error) {
      console.error('Error fetching cart count:', error);
      setCartError(true);
    } finally {
      setIsCartLoading(false);
    }
  };

  const handleCategoryClick = (category) => {
    fetchProducts(category);
  };

  const handleAddToCart = async (productId) => {
    if (!username) {
      console.error('Username is required to add items to the cart');
      return;
    }

    try {
      const response = await fetch('http://localhost:9090/api/cart/add', {
        credentials: 'include',
        method: 'POST',
        body: JSON.stringify({ username, productId }), //Include username and productId in the request
        headers: { 'content-Type': 'application/json' },
        // Include authToken as a cookie
      });

      if (response.ok) {
        fetchCartCount(); //Update cart count
      } else {
        console.error('Failed to add product to cart');
      }

    } catch (error) {
      console.error('Error adding product to cart:', error);
    }
  }


  return (
    <div className='customer-homepage'>
      <Header 
        cartCount={isCartLoading ? '...' : cartError ? 'Error' : cartCount}
        username={username} onChangeName={setUsername}
      />
      <nav className='navigation'>
        <CategoryNavigation onCategoryClick={handleCategoryClick} />
      </nav>
      <main className='main-content'>
        <ProductList products={products} onAddToCart={handleAddToCart}/>
      </main>
      <Footer />
    </div>
  )
}
