import { Link } from 'react-router-dom';
import { LiaCalendarSolid } from 'react-icons/lia';
import { Tag } from 'antd';
import { FaUser } from 'react-icons/fa';

import classNames from 'classnames/bind';
import styles from '~/styles/Post.module.scss';

const cx = classNames.bind(styles);

function Post({ className, data, layout = 'vertical' }) {
    return (
        <div
            className={cx('wrapper', className, {
                horizontal: layout === 'horizontal',
            })}
        >
            <div className={cx('postimg')}>
                <Link to="/">
                    <img
                        src={'http://libedu.huongvietedm.vn/Images/TinTuc/3.2_cah_ly.jpg'}
                        alt={'Sáng 3/2, Hà Nội và 3 địa phương khác có thêm 9 ca mắc COVID-19 ở cộng đồng'}
                    />
                </Link>
            </div>

            <div className={cx('postcontent')}>
                <ul className={cx('bookscategories')}>
                    <li>
                        <Link to="/" className="d-flex align-items-center">
                            <LiaCalendarSolid />
                            20/04/2021
                        </Link>
                    </li>
                    <li className="ms-2">
                        <Tag color="red">Mới nhất</Tag>
                    </li>
                </ul>

                <div className={cx('posttitle')}>
                    <Link to="/Home/Detail/21">
                        Training phan mem thu vien sáng 21/04 Training phan mem thu vien sáng 21/04
                    </Link>
                </div>

                <span className={cx('bookwriter')}>
                    <FaUser />
                    Tác giả:&nbsp;
                    <Link to="/"> admin </Link>
                </span>

                <div className={cx('postdes')}>
                    Bản tin 6h ngày 3/2 của Ban Chỉ đạo Quốc gia Phòng chống dịch COVID-19 cho biết có thêm 9 ca mắc mới
                    COVID-19 ở cộng đồng ghi...
                </div>
            </div>
        </div>
    );
}

export default Post;
