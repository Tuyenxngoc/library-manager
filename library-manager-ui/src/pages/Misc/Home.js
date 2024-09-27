import { Parallax } from 'react-parallax';

import { ImBooks, ImUserPlus } from 'react-icons/im';
import { FiUsers } from 'react-icons/fi';
import { BsShop } from 'react-icons/bs';

import { backgrounds } from '~/assets';
import ProductList from '~/components/ProductList';
import Slider from '~/components/Slider';

import classNames from 'classnames/bind';
import styles from '~/styles/Home.module.scss';
import { Button } from 'antd';
import FormattedNumber from '~/components/FormattedNumber';
import PostList from '~/components/PostList';

const cx = classNames.bind(styles);

const COUNTERS = [
    { title: 'Số ấn phẩm', count: 10620, className: 'books' },
    { title: 'Số tác giả', count: 2662, className: 'authors' },
    { title: 'Số nhà xuất bản', count: 183, className: 'publishers' },
    { title: 'Số bạn đọc', count: 1119, className: 'readers' },
];

function Home() {
    return (
        <>
            <Slider />

            <ProductList />

            <Parallax bgImage={backgrounds.bgparallax4} strength={500}>
                <div className="container py-5">
                    <div className="row">
                        {COUNTERS.map((item, index) => (
                            <div key={index} className={cx('col-3', 'collectioncounter', item.className)}>
                                <div className={cx('collectioncountericon')}>
                                    {index === 0 && <ImBooks />}
                                    {index === 1 && <FiUsers />}
                                    {index === 2 && <BsShop />}
                                    {index === 3 && <ImUserPlus />}
                                </div>
                                <div className={cx('titlepluscounter')}>
                                    <h2>{item.title}</h2>
                                    <h3>
                                        <FormattedNumber number={item.count} />
                                    </h3>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </Parallax>

            <ProductList />

            <Parallax bgImage={backgrounds.bgparallax5} strength={500}>
                <div className="container py-5">
                    <div className="row justify-content-center">
                        <div className="col-auto py-5">
                            <Button type="primary">Gửi nhận xét của bạn</Button>
                        </div>
                    </div>
                </div>
            </Parallax>

            <PostList />
        </>
    );
}

export default Home;
