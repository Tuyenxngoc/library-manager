import { HiOutlineBuildingOffice2 } from 'react-icons/hi2';
import { LuPhone } from 'react-icons/lu';
import { MdMailOutline } from 'react-icons/md';

import classNames from 'classnames/bind';
import styles from '~/styles/Footer.module.scss';
import SocialIcons from './SocialIcons';

const cx = classNames.bind(styles);

function Footer() {
    return (
        <footer className={cx('wrapper')}>
            <div className="container mb-5">
                <div className="row">
                    <div className="col-12">
                        <h1 className="py-5">Library Manager</h1>
                        <ul className={cx('contactinfo')}>
                            <li>
                                <HiOutlineBuildingOffice2 width={24} />
                                <span>Địa chỉ: Số 298 Đ. Cầu Diễn, Minh Khai, Bắc Từ Liêm, Hà Nội</span>
                            </li>
                            <li>
                                <LuPhone width={24} />
                                <span>Điện thoại: 02773851999</span>
                            </li>
                            <li>
                                <MdMailOutline width={24} />
                                <span>Email: c1lequydoncl.dongthap@moet.edu.vn</span>
                            </li>
                        </ul>

                        <SocialIcons />
                    </div>
                </div>
            </div>

            <div className={cx('bar')}>
                <div className="container">
                    <div className="row">
                        <div className="col">
                            <span>{new Date().getFullYear()} All Rights Reserved By © NHÓM 16</span>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
