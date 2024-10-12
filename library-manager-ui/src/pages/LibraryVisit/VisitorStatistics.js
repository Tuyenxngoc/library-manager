import { Button, Col, DatePicker, Form, Row, Space } from 'antd';
import { useNavigate } from 'react-router-dom';

function VisitorStatistics() {
    const naviagate = useNavigate();

    const onFinish = (values) => {
        console.log('Received values:', values);
    };

    return (
        <div>
            <h2>Sổ sách thư viện</h2>

            <Form name="visitor_statistics" onFinish={onFinish} layout="vertical">
                <Row gutter={16} justify="center">
                    <Col span={6}>
                        <Form.Item
                            label="Từ ngày"
                            name="fromDate"
                            rules={[{ required: true, message: 'Vui lòng chọn ngày!' }]}
                        >
                            <DatePicker className="w-100" />
                        </Form.Item>
                    </Col>
                    <Col span={6}>
                        <Form.Item
                            label="Đến ngày"
                            name="toDate"
                            rules={[{ required: true, message: 'Vui lòng chọn ngày!' }]}
                        >
                            <DatePicker className="w-100" />
                        </Form.Item>
                    </Col>
                </Row>
                <Row gutter={16} justify="center">
                    <Col span={6}>
                        <Form.Item>
                            <Space>
                                <Button type="primary" htmlType="submit">
                                    Thống kê
                                </Button>
                                <Button>In báo cáo</Button>
                                <Button>Xuất file</Button>
                            </Space>
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
            <Button onClick={() => naviagate('/admin/readers/access')}>Quay lại</Button>
        </div>
    );
}

export default VisitorStatistics;
