import React, { useState } from 'react';
import { Table, Button, Space, Modal, Input, Form, Switch, Image, Flex } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';

function SlideConfig() {
    const [slides, setSlides] = useState([
        {
            key: '1',
            title: 'Slide 1',
            description: 'Mô tả slide 1',
            imageUrl: 'https://via.placeholder.com/150',
            active: true,
        },
        {
            key: '2',
            title: 'Slide 2',
            description: 'Mô tả slide 2',
            imageUrl: 'https://via.placeholder.com/150',
            active: false,
        },
    ]);

    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingSlide, setEditingSlide] = useState(null);

    const handleDelete = (key) => {
        setSlides(slides.filter((slide) => slide.key !== key));
    };

    const handleEdit = (record) => {
        setEditingSlide(record);
        setIsModalVisible(true);
    };

    const handleAdd = () => {
        setEditingSlide(null);
        setIsModalVisible(true);
    };

    const handleSave = (values) => {
        if (editingSlide) {
            setSlides(slides.map((slide) => (slide.key === editingSlide.key ? { ...editingSlide, ...values } : slide)));
        } else {
            const newSlide = {
                ...values,
                key: Date.now().toString(),
                active: true,
            };
            setSlides([...slides, newSlide]);
        }
        setIsModalVisible(false);
    };

    const handleStatusChange = (key, checked) => {
        setSlides(slides.map((slide) => (slide.key === key ? { ...slide, active: checked } : slide)));
    };

    const columns = [
        {
            title: 'Tiêu đề',
            dataIndex: 'title',
            key: 'title',
        },
        {
            title: 'Mô tả',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Hình ảnh',
            dataIndex: 'imageUrl',
            key: 'imageUrl',
            render: (text) => <Image width={100} src={text} alt="Slide" />,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'active',
            key: 'active',
            render: (text, record) => (
                <Space>
                    {text ? 'Đang theo dõi' : 'Ngừng theo dõi'}
                    <Switch checked={text} onChange={(checked) => handleStatusChange(record.key, checked)} />
                </Space>
            ),
        },
        {
            title: 'Hành động',
            key: 'action',
            render: (text, record) => (
                <Space size="middle">
                    <Button icon={<EditOutlined />} onClick={() => handleEdit(record)}>
                        Sửa
                    </Button>
                    <Button icon={<DeleteOutlined />} danger onClick={() => handleDelete(record.key)}>
                        Xóa
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <>
            <Modal
                title={editingSlide ? 'Chỉnh sửa slide' : 'Thêm mới slide'}
                open={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                footer={null}
            >
                <Form
                    initialValues={editingSlide || { title: '', description: '', imageUrl: '' }}
                    onFinish={handleSave}
                    layout="vertical"
                >
                    <Form.Item
                        label="Tiêu đề"
                        name="title"
                        rules={[{ required: true, message: 'Tiêu đề là bắt buộc.' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="Mô tả"
                        name="description"
                        rules={[{ required: true, message: 'Mô tả là bắt buộc.' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="URL Hình ảnh"
                        name="imageUrl"
                        rules={[{ required: true, message: 'URL hình ảnh là bắt buộc.' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit">
                            {editingSlide ? 'Lưu thay đổi' : 'Thêm mới'}
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>

            <Flex className="py-2" wrap justify="space-between" align="center">
                <h2>Thiết lập slide</h2>
                <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
                    Thêm slide
                </Button>
            </Flex>

            <Table bordered columns={columns} dataSource={slides} />
        </>
    );
}

export default SlideConfig;
