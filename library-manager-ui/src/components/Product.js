import { Link } from 'react-router-dom';

import { FaShare } from 'react-icons/fa';

import classNames from 'classnames/bind';
import styles from '~/styles/Product.module.scss';
import { Button } from 'antd';

const cx = classNames.bind(styles);
function Product({ className, data }) {
    return (
        <div className={cx('postbook', className)}>
            <div className={cx('featureimg')}>
                <Link to={`book/${data.id}`}>
                    <img src={data.imageUrl} alt={data.title} />
                </Link>
                <div className={cx('tags')}>
                    {data.quantity === 0 && <div className={cx('tag-soldout')}>Hết hàng</div>}
                </div>
            </div>

            <div className={cx('content')}>
                <div className={cx('title')}>
                    <Link to={`book/${data.id}`}>{data.title}</Link>
                </div>

                <div className={cx('bookwriter')}>
                    <span>Tác giả: </span>
                    <Link to="#">Vũ Hữu Minh</Link>
                </div>
                <div className={cx('bookwriter')}>
                    <span>Đồng tác giả: </span>
                    <Link to="#">???</Link>
                </div>

                <Button type="primary" shape="round" icon={<FaShare />}>
                    Đăng Ký Mượn
                </Button>
            </div>
        </div>
    );
}

export default Product;
