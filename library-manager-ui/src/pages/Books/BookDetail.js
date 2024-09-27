import { Button } from 'antd';
import { Parallax } from 'react-parallax';
import { Link } from 'react-router-dom';
import images, { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import Product from '~/components/Product';
import SectionHeader from '~/components/SectionHeader';

import classNames from 'classnames/bind';
import styles from '~/styles/BookDetail.module.scss';

const cx = classNames.bind(styles);

function BookDetail() {
    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Thư viện sách',
            url: '/books',
        },
        {
            label: 'Sách tiếng việt',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Chi tiết sách</h1>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <Breadcrumb items={items} />
                            </div>
                        </div>
                    </div>
                </div>
            </Parallax>

            <div className="container sectionspace">
                <div className="row mb-4">
                    <div className="col-3">
                        <Button block>Sách được mượn nhiều nhất</Button>
                    </div>
                    <div className="col-9">
                        <div className="row">
                            <div className="col-4">
                                <Product
                                    data={{
                                        id: 1,
                                        imageUrl:
                                            'https://product.hstatic.net/200000343865/product/sap-lon_3a4cfc5f21e04c108c48f266fef21902_large.jpg',
                                        title: 'Sach di dong',
                                    }}
                                />
                            </div>
                            <div className="col-8">
                                <div className={cx('productcontent')}>
                                    <ul className={cx('bookscategories')}>
                                        <li>Số lượng sách còn trong thư viện: 1</li>
                                    </ul>

                                    <div className={cx('booktitle')}>
                                        <h3>Tiếng Việt 2 tập 2</h3>
                                    </div>

                                    <span className={cx('bookwriter')}>
                                        Tác giả: <Link to="/">Nguyễn Thị Ly Kha</Link>
                                    </span>

                                    <div className={cx('share')}>
                                        <span>Share:</span>
                                        <ul className="socialicons">
                                            <li className="facebook">
                                                <a href="/">
                                                    <img width={30} src={images.facebook} alt="icon" />
                                                </a>
                                            </li>
                                            <li className="twitter">
                                                <a href="/">
                                                    <img width={30} src={images.twitter} alt="icon" />
                                                </a>
                                            </li>
                                            <li className="linkedin">
                                                <a href="/">
                                                    <img width={30} src={images.linkedin} alt="icon" />
                                                </a>
                                            </li>
                                            <li className="googleplus">
                                                <a href="/">
                                                    <img width={30} src={images.googleplus} alt="icon" />
                                                </a>
                                            </li>
                                            <li className="rss">
                                                <a href="/">
                                                    <img width={30} src={images.rss} alt="icon" />
                                                </a>
                                            </li>
                                        </ul>
                                    </div>

                                    <div className={cx('description')}>
                                        <p>Chưa có mô tả cho cuốn sách này</p>
                                    </div>

                                    <SectionHeader title="Chi tiết sách" />

                                    <ul className={cx('productinfo')}>
                                        <li>
                                            <span>Định dạng:</span>
                                            <span>PDF</span>
                                        </li>
                                        <li>
                                            <span>Số trang:</span>
                                            <span>148</span>
                                        </li>
                                        <li>
                                            <span>Kích thước:</span>
                                            <span>19x26.5cmm</span>
                                        </li>
                                        <li>
                                            <span>Năm xuất bản:</span>
                                            <span>2021</span>
                                        </li>
                                        <li>
                                            <span>Nhà xuất bản:</span>
                                            <span>Giáo dục</span>
                                        </li>
                                        <li>
                                            <span>Ngôn ngữ:</span>
                                            <span>Tiếng Trung</span>
                                        </li>
                                        <li>
                                            <span>ISBN:</span>
                                            <span></span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default BookDetail;
