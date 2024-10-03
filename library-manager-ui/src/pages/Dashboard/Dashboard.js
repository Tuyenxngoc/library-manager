import { FaArrowAltCircleRight } from 'react-icons/fa';
import { FaEdit } from 'react-icons/fa';
import { FaThumbtack } from 'react-icons/fa';
import { FaClock } from 'react-icons/fa';
import { FaBan } from 'react-icons/fa';
import { Link } from 'react-router-dom';
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
                        <div>{label}</div>
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
    ];

    const pieData = [
        { name: 'Đang cho mượn', value: 75 },
        { name: 'Quá hạn', value: 25 },
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

            <div className="mt-4">
                <h3>Biểu đồ thống kê số lượng ấn phẩm theo phân loại</h3>
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

            <div className="mt-4">
                <h3>Tình hình mượn trả</h3>
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
    );
}

export default Dashboard;
