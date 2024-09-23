import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

import Product from './Product';
import SectionHeader from './SectionHeader';
import jsonData from '../data/products.json';

import classNames from 'classnames/bind';
import styles from '~/styles/ProductList.module.scss';

const cx = classNames.bind(styles);

function ProductList() {
    const sliderRef = useRef(null);
    const navigate = useNavigate();

    const [products, setProducts] = useState(jsonData);

    const settings = {
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 5,
        slidesToScroll: 1,
        swipeToSlide: true,
    };

    const goToNextSlide = () => {
        sliderRef.current.slickNext();
    };

    const goToPrevSlide = () => {
        sliderRef.current.slickPrev();
    };

    const handleViewAll = () => {
        navigate('/all-products');
    };

    return (
        <section className={cx('wrapper')}>
            <div className="container">
                <div className="row mb-3">
                    <div className="col-12">
                        <SectionHeader
                            subtitle="Lựa chọn của mọi người"
                            title="Sách được mượn nhiều nhất"
                            onPrev={goToPrevSlide}
                            onNext={goToNextSlide}
                            onViewAll={handleViewAll}
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="col-12">
                        <Slider ref={sliderRef} {...settings}>
                            {products.map((data, index) => (
                                <Product className="mx-2 my-1" key={index} data={data} />
                            ))}
                        </Slider>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default ProductList;
