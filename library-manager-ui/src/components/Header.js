import { Link } from 'react-router-dom';

import { MdMailOutline, MdHelpOutline } from 'react-icons/md';
import { IoIosLogOut } from 'react-icons/io';
import { FaUser, FaRegUser } from 'react-icons/fa6';
import { FaRegEdit, FaHistory, FaAngleDown } from 'react-icons/fa';

import useAuth from '~/hooks/useAuth';

import { Dropdown, Space, Input, Select } from 'antd';
import 'bootstrap/dist/js/bootstrap.bundle';

import classNames from 'classnames/bind';
import styles from '~/styles/Header.module.scss';

const cx = classNames.bind(styles);

const { Search } = Input;

const options = [
    {
        label: 'Nhan đề',
        value: 'title',
    },
    {
        label: 'Tác giả',
        value: 'author',
    },
    {
        label: 'Nhà xuất bản',
        value: 'publisher',
    },
    {
        label: 'Năm xuất bản',
        value: 'publicationYear',
    },
    {
        label: 'Số ISBN',
        value: 'isbn',
    },
];

function Header() {
    const { isAuthenticated, logout } = useAuth();

    const items = isAuthenticated
        ? [
              {
                  key: '1',
                  label: <Link to="/">Thông tin cá nhân</Link>,
                  icon: <FaUser />,
              },
              {
                  key: '2',
                  label: <Link to="/">Lưu thông</Link>,
                  icon: <FaHistory />,
              },
              {
                  key: '3',
                  label: <Link to="/">Đã đăng ký mượn</Link>,
                  icon: <FaRegEdit />,
              },
              {
                  key: '4',
                  label: 'Đăng xuất',
                  icon: <IoIosLogOut />,
                  onClick: logout,
              },
          ]
        : [
              {
                  key: '4',
                  label: <Link to="/login">Đăng nhập</Link>,
                  icon: <IoIosLogOut />,
              },
          ];

    return (
        <header>
            <div className={cx('wrapper')}>
                <div className="container">
                    <div className="row justify-content-between align-items-center">
                        <div className="col-auto">
                            <ul className={cx('list')}>
                                <li>
                                    <a href="/">
                                        <MdMailOutline />
                                        Liên hệ
                                    </a>
                                </li>
                                <li>
                                    <a href="/">
                                        <MdHelpOutline />
                                        Trợ giúp
                                    </a>
                                </li>
                            </ul>
                        </div>
                        <div className="col-auto">
                            <Dropdown
                                menu={{
                                    items,
                                }}
                            >
                                <Space>
                                    <FaRegUser />
                                    Tài khoản
                                    <FaAngleDown />
                                </Space>
                            </Dropdown>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container py-4">
                <div className="row align-items-center">
                    <div className="col-4">
                        <h1>Library Manager</h1>
                    </div>
                    <div className="col-8">
                        <Space.Compact className="w-100">
                            <Select size="large" defaultValue="title" options={options} />
                            <Search
                                size="large"
                                name="search"
                                placeholder="Nhập nội dung tìm kiếm của bạn ở đây"
                                allowClear
                            />
                        </Space.Compact>

                        <div className="text-lg-end">
                            <a href="/Search" style={{ color: '#55acee', fontSize: '13px' }}>
                                + Tìm kiếm nâng cao
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <nav className="navbar navbar-expand-lg" style={{ backgroundColor: 'var(--primary-color)' }}>
                <div className="container">
                    <Link className="navbar-brand text-white" to="/">
                        Trang chủ
                    </Link>
                    <button
                        className="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation"
                    >
                        <span className="navbar-toggler-icon" />
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item">
                                <Link className="nav-link px-4 text-white active" aria-current="page" to="/news">
                                    Tin tức
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link px-4 text-white" to="/about">
                                    Giới thiệu
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link px-4 text-white" to="/holiday-schedule">
                                    Lịch nghỉ lễ
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link px-4 text-white" to="/rules">
                                    Nội quy
                                </Link>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
    );
}

export default Header;
