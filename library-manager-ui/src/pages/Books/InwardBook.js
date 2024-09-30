import { useNavigate } from 'react-router-dom';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FaRegTrashAlt } from 'react-icons/fa';

import { Button, Flex, Popconfirm, Space, Table, Tag } from 'antd';

function InwardBook() {
    const navigate = useNavigate();

    const handleDeleteEntity = async (id) => {};

    const columns = [
        {
            title: 'Số phiếu nhập',
            dataIndex: 'name',
            key: 'name',
            render: (text) => <a>{text}</a>,
        },
        {
            title: 'Số vào tổng quát',
            dataIndex: 'age',
            key: 'age',
        },
        {
            title: 'Ngày nhập',
            dataIndex: 'address',
            key: 'address',
        },
        {
            title: 'Nguồn cấp phát',
            key: 'tags',
            dataIndex: 'tags',
            render: (_, { tags }) => (
                <>
                    {tags.map((tag) => {
                        let color = tag.length > 5 ? 'geekblue' : 'green';
                        if (tag === 'loser') {
                            color = 'volcano';
                        }
                        return (
                            <Tag color={color} key={tag}>
                                {tag.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            ),
        },
        {
            title: 'Lý do nhập',
            dataIndex: 'address',
            key: 'address',
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Space>
                    <Button type="text" icon={<MdOutlineModeEdit />} onClick={() => navigate(`edit/${record.id}`)} />
                    <Popconfirm
                        title="Thông báo"
                        description={
                            <div>
                                Bạn có chắc muốn xóa <b>{record.fullName}</b> không?
                            </div>
                        }
                        onConfirm={() => handleDeleteEntity(record.id)}
                        okText="Xóa"
                        cancelText="Hủy"
                    >
                        <Button type="text" danger icon={<FaRegTrashAlt />} />
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <Flex className="py-2" wrap justify="space-between" align="center">
                <h2>Danh sách phiếu nhập</h2>
                <Button type="primary" onClick={() => navigate('new')}>
                    Lập phiếu nhập
                </Button>
            </Flex>
        </div>
    );
}

export default InwardBook;
