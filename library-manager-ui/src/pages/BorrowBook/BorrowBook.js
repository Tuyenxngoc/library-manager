import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Drawer, Flex, Input, message, Popconfirm, Select, Space, Table } from 'antd';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FaRegTrashAlt, FaPrint } from 'react-icons/fa';

import queryString from 'query-string';

import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import {
    deleteBorrowReceipt,
    getBorrowReceiptDetails,
    getBorrowReceipts,
    printBorrowReceipts,
} from '~/services/borrowReceiptService';

const options = [
    { value: 'receiptNumber', label: 'Số phiếu mượn' },
    { value: 'cardNumber', label: 'Số thẻ bạn đọc' },
    { value: 'fullName', label: 'Tên bạn đọc' },
];

function BorrowBook() {
    const navigate = useNavigate();

    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

    const [searchInput, setSearchInput] = useState('');
    const [activeFilterOption, setActiveFilterOption] = useState(options[0].value);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    const [messageApi, contextHolder] = message.useMessage();

    const [isDrawerOpen, setIsDrawerOpen] = useState(false);
    const [receiptDetails, setReceiptDetails] = useState(null);
    const [loadingDetails, setLoadingDetails] = useState(false);

    const [selectedRowKeys, setSelectedRowKeys] = useState([]);

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

    const handleSearch = (searchBy, keyword) => {
        setFilters((prev) => ({
            ...prev,
            pageNum: 1,
            searchBy: searchBy || activeFilterOption,
            keyword: keyword || searchInput,
        }));
    };

    const showDrawer = async (receiptId) => {
        setLoadingDetails(true);
        setIsDrawerOpen(true);

        try {
            const response = await getBorrowReceiptDetails(receiptId);
            setReceiptDetails(response.data.data);
        } catch (error) {
            message.error('Không thể tải chi tiết phiếu mượn.');
        } finally {
            setLoadingDetails(false);
        }
    };

    const closeDrawer = () => {
        setIsDrawerOpen(false);
        setReceiptDetails(null);
    };

    const handleDeleteEntity = async (id) => {
        try {
            const response = await deleteBorrowReceipt(id);
            if (response.status === 200) {
                setEntityData((prev) => prev.filter((a) => a.id !== id));

                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi xóa.';
            messageApi.error(errorMessage);
        }
    };

    const handlePrintBorrow = async () => {
        if (selectedRowKeys.length === 0) {
            messageApi.info('Vui lòng chọn ít nhất một phiếu.');
            return;
        }
        setIsLoading(true);
        try {
            const response = await printBorrowReceipts({
                schoolName: 'Trường Đại học Công nghiệp Hà Nội',
                borrowIds: selectedRowKeys,
            });

            if (response.status === 200) {
                // Chuyển đổi mảng byte thành Blob
                const pdfBlob = new Blob([response.data], { type: 'application/pdf' });
                const url = URL.createObjectURL(pdfBlob);

                // Mở hoặc tải xuống file PDF
                const newTab = window.open(url, '_blank');
                newTab.focus(); // Đưa tab mới lên trước

                // Giải phóng URL sau khi sử dụng
                URL.revokeObjectURL(url);
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi xuất dữ liệu.';
            messageApi.error(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getBorrowReceipts(params);
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

    const rowSelection = {
        onChange: (selectedRowKeys) => {
            setSelectedRowKeys(selectedRowKeys);
        },
        getCheckboxProps: (record, index) => ({
            name: record.id,
        }),
    };

    const columns = [
        {
            title: 'Số phiếu mượn',
            dataIndex: 'receiptNumber',
            key: 'receiptNumber',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => (
                <span style={{ color: '#0997eb', cursor: 'pointer' }} onClick={() => showDrawer(record.id)}>
                    {text}
                </span>
            ),
        },
        {
            title: 'Số thẻ bạn đọc',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
        },
        {
            title: 'Tên bạn đọc',
            dataIndex: 'fullName',
            key: 'fullName',
        },
        {
            title: 'Số lượng mượn',
            dataIndex: 'books',
            key: 'books',
        },
        {
            title: 'Ngày mượn',
            dataIndex: 'borrowDate',
            key: 'borrowDate',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ngày hẹn trả',
            dataIndex: 'dueDate',
            key: 'dueDate',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ngày trả',
            dataIndex: 'returnDate',
            key: 'returnDate',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Ghi chú',
            dataIndex: 'note',
            key: 'note',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Space>
                    <Button type="text" icon={<MdOutlineModeEdit />} onClick={() => navigate(`edit/${record.id}`)} />
                    <Popconfirm
                        title="Xóa phiếu mượn"
                        description="Bạn có chắc muốn xóa phiếu mượn này không?"
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

            <Drawer title="Chi tiết phiếu mượn" width={720} onClose={closeDrawer} open={isDrawerOpen}>
                {loadingDetails ? (
                    <div>Đang tải...</div>
                ) : receiptDetails ? (
                    <div className="container">
                        <div className="row mb-4">
                            <div className="col-md-6">
                                <div className="fw-bold">Tên người mượn:</div>
                                <p>{receiptDetails.fullName}</p>
                            </div>
                            <div className="col-md-6">
                                <div className="fw-bold">Ngày mượn:</div>
                                <p>{receiptDetails.borrowDate}</p>
                            </div>
                            <div className="col-md-6">
                                <div className="fw-bold">Ngày hẹn trả:</div>
                                <p>{receiptDetails.dueDate}</p>
                            </div>
                            <div className="col-md-6">
                                <div className="fw-bold">Tình trạng:</div>
                                <p className={`text-${receiptDetails.status === 'Đã trả' ? 'success' : 'danger'}`}>
                                    {receiptDetails.status}
                                </p>
                            </div>
                        </div>

                        <table className="table table-striped table-hover">
                            <thead className="table-dark">
                                <tr>
                                    <th>Nhan đề</th>
                                    <th>Số đăng ký cá biệt</th>
                                    <th>Tình trạng</th>
                                </tr>
                            </thead>
                            <tbody>
                                {receiptDetails.books.map((book, index) => (
                                    <tr key={index}>
                                        <td>{book.title}</td>
                                        <td>{book.bookCode}</td>
                                        <td>{book.returned ? 'Đã trả' : 'Chưa trả'}</td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colSpan="3" className="text-end fw-bold">
                                        Tổng: {receiptDetails.books.length} đầu sách
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                ) : (
                    <div>Không có dữ liệu.</div>
                )}
            </Drawer>

            <Flex wrap justify="space-between" align="center">
                <h2>Mượn sách</h2>
                <Space>
                    <Space.Compact className="my-2">
                        <Select
                            options={options}
                            disabled={isLoading}
                            value={activeFilterOption}
                            onChange={(value) => setActiveFilterOption(value)}
                        />
                        <Input
                            allowClear
                            name="searchInput"
                            placeholder="Nhập từ cần tìm..."
                            value={searchInput}
                            disabled={isLoading}
                            onChange={(e) => setSearchInput(e.target.value)}
                        />
                        <Button type="primary" loading={isLoading} onClick={() => handleSearch()}>
                            Tìm
                        </Button>
                    </Space.Compact>

                    <Button type="primary" onClick={() => navigate('new')}>
                        Lập phiếu mượn
                    </Button>

                    <Button icon={<FaPrint />} onClick={handlePrintBorrow}></Button>
                </Space>
            </Flex>

            <Table
                bordered
                rowKey="id"
                scroll={{ x: 'max-content' }}
                dataSource={entityData}
                columns={columns}
                loading={isLoading}
                onChange={handleSortChange}
                rowSelection={rowSelection}
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

export default BorrowBook;
