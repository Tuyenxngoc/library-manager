import { useEffect, useMemo, useState } from 'react';
import queryString from 'query-string';
import { Button, Flex, Form, Input, message, Space, Table } from 'antd';
import { IoMdPersonAdd } from 'react-icons/io';
import { FaRegClock, FaList, FaSignOutAlt } from 'react-icons/fa';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { closeLibrary, createLibraryVisit, getLibraryVisits } from '~/services/libraryVisitService';
import { useNavigate } from 'react-router-dom';

function LibraryVisit() {
    const naviagate = useNavigate();
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    const [messageApi, contextHolder] = message.useMessage();

    const [form] = Form.useForm();

    const currentDate = useMemo(
        () =>
            new Date().toLocaleDateString('vi-VN', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
            }),
        [],
    );

    const handleChangePage = (newPage) => {
        setFilters((prev) => ({ ...prev, pageNum: newPage }));
    };

    const handleChangeRowsPerPage = (current, size) => {
        setFilters((prev) => ({
            ...prev,
            pageNum: 1,
            pageSize: size,
        }));
    };

    const handleSortChange = (pagination, filters, sorter) => {
        const sortOrder = sorter.order === 'ascend' ? true : sorter.order === 'descend' ? false : undefined;
        setFilters((prev) => ({
            ...prev,
            sortBy: sorter.field,
            isAscending: sortOrder,
        }));
    };

    const handleCreateEntity = async (values) => {
        try {
            const response = await createLibraryVisit(values);
            if (response.status === 200 || response.status === 201) {
                const { data, message } = response.data.data;
                messageApi.success(message);

                setEntityData((prevData) => {
                    const existingIndex = prevData.findIndex((item) => item.id === data.id);
                    if (existingIndex !== -1) {
                        return prevData.map((item, index) => {
                            if (index === existingIndex) {
                                return data;
                            }
                            return item;
                        });
                    } else {
                        return [data, ...prevData.slice(0, -1)];
                    }
                });
                form.resetFields();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật dữ liệu.';
            form.setFields([
                {
                    name: 'cardNumber',
                    errors: [errorMessage],
                },
            ]);
        }
    };

    const handleCloseLibrary = async () => {
        try {
            const response = await closeLibrary();
            if (response.status === 200) {
                const { message } = response.data.data;
                messageApi.success(message);

                handleChangePage(1);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật dữ liệu.';
            messageApi.error(errorMessage);
        }
    };

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getLibraryVisits(params);
                const { meta, items } = response.data.data;
                setEntityData(items);
                setMeta(meta);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEntities();
    }, [filters]);

    const columns = [
        {
            title: 'Số thẻ',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
        },
        {
            title: 'Họ tên',
            dataIndex: 'fullName',
            key: 'fullName',
        },
        {
            title: 'Loại thẻ',
            dataIndex: 'cardType',
            key: 'cardType',
        },
        {
            title: 'Giờ vào',
            dataIndex: 'entryTime',
            key: 'entryTime',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Giờ ra',
            dataIndex: 'exitTime',
            key: 'exitTime',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Button
                    type="text"
                    icon={<FaSignOutAlt />}
                    disabled={record.exitTime !== null}
                    onClick={() => handleCreateEntity({ cardNumber: record.cardNumber })}
                />
            ),
        },
    ];

    if (errorMessage) {
        return (
            <div className="alert alert-danger p-2" role="alert">
                Lỗi: {errorMessage}
            </div>
        );
    }

    return (
        <div>
            {contextHolder}

            <h2>Thông tin bạn đọc</h2>
            <b>Ngày: {currentDate}</b>
            <Flex className="py-2" wrap justify="space-between" align="center">
                <Form form={form} onFinish={handleCreateEntity} layout="inline">
                    <Form.Item
                        label="Số thẻ"
                        name="cardNumber"
                        rules={[{ required: true, message: 'Vui lòng nhập số thẻ!' }]}
                    >
                        <Input placeholder="Nhập số thẻ" />
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" icon={<IoMdPersonAdd />} htmlType="submit">
                            Cập nhật
                        </Button>
                    </Form.Item>
                </Form>

                <Space>
                    <Button icon={<FaRegClock />} onClick={handleCloseLibrary}>
                        Đóng cửa
                    </Button>
                    <Button type="link" icon={<FaList />} onClick={() => naviagate('statistics')}>
                        Thống kê số lượt bạn đọc
                    </Button>
                </Space>
            </Flex>

            <h2>Danh sách bạn đọc vào thư viện</h2>

            <Table
                bordered
                rowKey="id"
                dataSource={entityData}
                columns={columns}
                loading={isLoading}
                onChange={handleSortChange}
                pagination={{
                    current: filters.pageNum,
                    pageSize: filters.pageSize,
                    total: meta.totalElements,
                    onChange: handleChangePage,
                    showSizeChanger: true,
                    onShowSizeChange: handleChangeRowsPerPage,
                }}
            />
        </div>
    );
}

export default LibraryVisit;
