import { Link } from 'react-router-dom';
import { Timeline } from 'antd';
import { FaEdit, FaThumbtack, FaBook, FaClock, FaBan, FaArrowAltCircleRight } from 'react-icons/fa';
import { IoBarChartSharp } from 'react-icons/io5';
import { FaChartPie } from 'react-icons/fa6';

import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid,
    Legend,
    ResponsiveContainer,
    PieChart,
    Pie,
    Cell,
} from 'recharts';
import classNames from 'classnames/bind';
import styles from '~/styles/Dashboard.module.scss';

const cx = classNames.bind(styles);

const Card = ({ icon: Icon, count, label, link, color = 'white' }) => {
    return (
        <div className="card text-white" style={{ borderColor: color }}>
            <div className="card-header" style={{ backgroundColor: color }}>
                <div className="row">
                    <div className="col-3">
                        <Icon className="fs-1" />
                    </div>
                    <div className="col-9 text-end">
                        <div className="fs-1">{count}</div>
                        <div className="fw-bold">{label}</div>
                    </div>
                </div>
            </div>
            <Link to={link} style={{ color: color }}>
                <div className="card-footer">
                    <span className="float-start">Xem chi tiết</span>
                    <span className="float-end">
                        <FaArrowAltCircleRight />
                    </span>
                    <div className="clearfix" />
                </div>
            </Link>
        </div>
    );
};

function Dashboard() {
    const data = [
        { category: 'Sách', count: 10 },
        { category: 'Tạp chí', count: 5 },
        { category: 'Báo', count: 15 },
        { category: 'Tài liệu tham khảo', count: 8 },
        { category: 'Sách', count: 10 },
        { category: 'Tạp chí', count: 5 },
        { category: 'Báo', count: 15 },
        { category: 'Tài liệu tham khảo', count: 8 },
        { category: 'Sách', count: 10 },
        { category: 'Tạp chí', count: 5 },
        { category: 'Báo', count: 15 },
        { category: 'Tài liệu tham khảo', count: 8 },
    ];

    const pieData = [
        { name: 'Đang cho mượn', value: 75 },
        { name: 'Quá hạn', value: 25 },
    ];

    const mostBorrowedPublications = [
        {
            title: 'The Great Gatsby',
            date: '2023-01-15',
            description: 'Một câu chuyện về sự xa hoa và những giấc mơ không thành của Mỹ thập niên 1920.',
        },
        {
            title: 'To Kill a Mockingbird',
            date: '2023-02-10',
            description: 'Cuốn tiểu thuyết kinh điển về nạn phân biệt chủng tộc và công lý ở miền Nam nước Mỹ.',
        },
        {
            title: '1984',
            date: '2023-03-20',
            description: 'Tác phẩm kinh điển của George Orwell về một xã hội kiểm soát toàn diện.',
        },
        {
            title: 'Pride and Prejudice',
            date: '2023-04-25',
            description: 'Câu chuyện lãng mạn nổi tiếng của Jane Austen về tình yêu và định kiến.',
        },
    ];

    // Màu sắc cho các phần của biểu đồ
    const COLORS = ['#5cb85c', '#d9534f'];

    return (
        <div>
            <div className="row g-3">
                <div className="col-lg-3 col-md-6">
                    <Card icon={FaEdit} count={0} label="Yêu cầu mượn" link="/admin/home" color="#337ab7" />
                </div>
                <div className="col-lg-3 col-md-6">
                    <Card icon={FaThumbtack} count={0} label="Số đang mượn" link="/admin/home" color="#5cb85c" />
                </div>
                <div className="col-lg-3 col-md-6">
                    <Card icon={FaClock} count={0} label="Đến hạn trả" link="/admin/home" color="#f0ad4e" />
                </div>
                <div className="col-lg-3 col-md-6">
                    <Card icon={FaBan} count={0} label="Quá hạn trả" link="/admin/home" color="#d9534f" />
                </div>
            </div>

            <div className={cx('panel')}>
                <div className={cx('heading')}>
                    <IoBarChartSharp /> &nbsp; <b>Biểu đồ thống kê số lượng ấn phẩm theo phân loại</b>
                </div>
                <div className={cx('body')}>
                    <ResponsiveContainer width="100%" height={300}>
                        <BarChart data={data}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="category" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="count" fill="#8884d8" />
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>

            <div className={cx('panel')}>
                <div className={cx('heading')}>
                    <FaChartPie /> &nbsp; <b>Tình hình mượn trả</b>
                </div>
                <div className={cx('body')}>
                    <ResponsiveContainer width="100%" height={300}>
                        <PieChart>
                            <Pie
                                data={pieData}
                                dataKey="value"
                                nameKey="name"
                                cx="50%"
                                cy="50%"
                                outerRadius={80}
                                fill="#8884d8"
                            >
                                {pieData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index]} />
                                ))}
                            </Pie>
                            <Tooltip />
                        </PieChart>
                    </ResponsiveContainer>
                </div>
            </div>

            <div className={cx('panel')}>
                <div className={cx('heading')}>
                    <FaBook /> &nbsp;<b>Ấn phẩm được mượn nhiều nhất</b>
                </div>
                <div className={cx('body')}>
                    <Timeline mode="alternate">
                        {mostBorrowedPublications.map((publication, index) => (
                            <Timeline.Item
                                key={index}
                                color={index % 2 === 0 ? 'gray' : 'green'}
                                dot={<FaBook style={{ fontSize: '16px' }} />}
                            >
                                <strong>{publication.title}</strong> - <em>{publication.date}</em>
                                <p>{publication.description}</p>
                            </Timeline.Item>
                        ))}
                    </Timeline>
                </div>
            </div>
        </div>
    );
}

export default Dashboard;
