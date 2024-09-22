import SectionHeader from './SectionHeader';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import Slider from 'react-slick';
import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Product from './Product';

import classNames from 'classnames/bind';
import styles from '~/styles/ProductList.module.scss';

const cx = classNames.bind(styles);

function ProductList() {
    const sliderRef = useRef(null);
    const navigate = useNavigate();

    const [products, setProducts] = useState([1, 2, 3, 4, 5, 5, 2]);

    const settings = {
        dots: false,
        infinite: true,
        speed: 500,
        slidesToShow: 5,
        slidesToScroll: 1,
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
                <div className="row">
                    <div className="col">
                        <SectionHeader
                            subtitle="Lựa chọn của mọi người"
                            title="Sách được mượn nhiều nhất"
                            onPrev={goToPrevSlide}
                            onNext={goToNextSlide}
                            onViewAll={handleViewAll}
                        />

                        <Slider ref={sliderRef} {...settings}>
                            {products.map((data, index) => (
                                <Product key={index} data={data} />
                            ))}
                        </Slider>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default ProductList;
