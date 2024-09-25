import { HiOutlineBuildingOffice2 } from 'react-icons/hi2';
import { LuPhone } from 'react-icons/lu';
import { MdMailOutline } from 'react-icons/md';

import classNames from 'classnames/bind';
import styles from '~/styles/Footer.module.scss';
import images from '~/assets';

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
