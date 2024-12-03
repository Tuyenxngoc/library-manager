import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from 'antd';
import { FaShare } from 'react-icons/fa';
import classNames from 'classnames/bind';
import styles from '~/styles/Product.module.scss';
import images from '~/assets';
import { addToCart } from '~/services/cartService';

const cx = classNames.bind(styles);
function Product({ className, data, messageApi }) {
    const bookUrl = `/books/${data.id}`;

    const handleAddToCart = async (id) => {
        try {
            const response = await addToCart(id);
            if (response.status === 201) {
                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra mượn sách.';
            messageApi.error(errorMessage);
        }
    };

    return (
        <div className={cx('postbook', className)}>
            <div className={cx('featureimg')}>
                <Link to={bookUrl}>
                    <img src={data.imageUrl || images.placeimg} alt={data.title} />
                </Link>
                <div className={cx('tags')}>
                    {data.quantity === 0 && <div className={cx('tag-soldout')}>Hết sách</div>}
                    <div className={cx('tag-saleoff')}>HOT</div>
                </div>
            </div>

            <div className={cx('content')}>
                <div className={cx('title')}>
                    <Link to={bookUrl}>{data.title}</Link>
                </div>

                <div className={cx('bookwriter')}>
                    <span>Tác giả: </span>
                    {data.authors.length > 0
                        ? data.authors.map((author, index) => (
                              <React.Fragment key={author.id || index}>
                                  <Link to={`/author/${author.id}`}>{author.name}</Link>
                                  {index < data.authors.length - 1 && ', '}
                              </React.Fragment>
                          ))
                        : 'Không xác định'}
                </div>

                <Button type="primary" shape="round" icon={<FaShare />} onClick={() => handleAddToCart(data.id)}>
                    Đăng Ký Mượn
                </Button>
            </div>
        </div>
    );
}

export default Product;
