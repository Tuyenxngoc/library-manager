import { Carousel } from 'antd';

import { slides } from '~/assets';

import classNames from 'classnames/bind';
import styles from '~/styles/slider.module.scss';

const cx = classNames.bind(styles);

function Slider() {
    return (
        <Carousel arrows autoplay>
            {slides.map((image, index) => (
                <div className={cx('item')} key={index}>
                    <img src={image} alt={`Slide ${index}`} />
                </div>
            ))}
        </Carousel>
    );
}

export default Slider;
