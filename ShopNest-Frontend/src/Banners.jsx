import React, { useEffect, useState } from 'react'

export default function Banners() {

    const urls = [
        'https://rukminim2.flixcart.com/fk-p-flap/3240/540/image/f23d56e0b5d37cff.jpg?q=60',
        'https://rukminim2.flixcart.com/fk-p-flap/3240/540/image/c9c7b904fae51a5a.jpg?q=60',
        'https://rukminim2.flixcart.com/fk-p-flap/3240/540/image/0c399ab8a196f69d.jpeg?q=60',
    ]
    const [currentIndex, setCurrentIndex] = useState(0);

  const prevSlide = () => {
    setCurrentIndex((prev) => (prev === 0 ? urls.length - 1 : prev - 1));
  };

  const nextSlide = () => {
    setCurrentIndex((prev) => (prev === urls.length - 1 ? 0 : prev + 1));
  };

  return (
    <div className="banners-main">
      <div
        className="banners-container"
        style={{ transform: `translateX(-${currentIndex * 100}%)` }}
      >
        {urls.map((url, index) => (
          <div className="banner-slide" key={index}>
            <img src={url} alt={`banner-${index}`} />
          </div>
        ))}
      </div>
      <button className="prev-btn" onClick={prevSlide}>❮</button>
      <button className="next-btn" onClick={nextSlide}>❯</button>
    </div>
  );
}
