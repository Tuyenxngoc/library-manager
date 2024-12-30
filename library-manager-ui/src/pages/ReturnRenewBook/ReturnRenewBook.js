import { useEffect, useState } from 'react';
import { Button, Flex, Input, message, Modal, Select, Space, Table } from 'antd';
import queryString from 'query-string';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { getBookBorrows, reportLostBooks, returnBooks } from '~/services/bookBorrowService';

const options = [
    { value: 'receiptNumber', label: 'Số phiếu mượn' },
    { value: 'cardNumber', label: 'Số thẻ bạn đọc' },
    { value: 'fullName', label: 'Tên bạn đọc' },
];

function ReturnRenewBook() {
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState({ ...INITIAL_FILTERS, status: ['NOT_RETURNED'] });

    const [entityData, setEntityData] = useState(null);

    const [searchInput, setSearchInput] = useState('');
    const [activeFilterOption, setActiveFilterOption] = useState(options[0].value);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    const [messageApi, contextHolder] = message.useMessage();

    const [isReturnModalOpen, setIsReturnModalOpen] = useState(false);
    const [isLostModalOpen, setIsLostModalOpen] = useState(false);
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

    const handleReturnBook = async (bookId) => {
        try {
            setIsLoading(true);
            const response = await returnBooks([bookId]);
            if (response.status === 200) {
                messageApi.success('Trả sách thành công!');
                handleChangePage(1);
            }
        } catch (error) {
            messageApi.error(`Lỗi khi trả sách: ${error.response?.data?.message || error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    const handleReportLostBook = async (bookId) => {
        try {
            setIsLoading(true);
            const response = await reportLostBooks([bookId]);
            if (response.status === 200) {
                messageApi.success('Báo mất sách thành công!');
                handleChangePage(1);
            }
        } catch (error) {
            messageApi.error(`Lỗi khi báo mất sách: ${error.response?.data?.message || error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBulkReturnBooks = async () => {
        try {
            setIsLoading(true);
            if (selectedRowKeys.length === 0) {
                messageApi.warning('Vui lòng chọn ít nhất một sách để trả.');
                return;
            }

            const response = await returnBooks(selectedRowKeys);
            if (response.status === 200) {
                messageApi.success('Trả sách hàng loạt thành công!');
                setSelectedRowKeys([]);
                setIsReturnModalOpen(false);
                handleChangePage(1);
            }
        } catch (error) {
            messageApi.error(`Lỗi khi trả sách hàng loạt: ${error.response?.data?.message || error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBulkReportLostBooks = async () => {
        try {
            setIsLoading(true);
            if (selectedRowKeys.length === 0) {
                messageApi.warning('Vui lòng chọn ít nhất một sách để báo mất.');
                return;
            }

            const response = await reportLostBooks(selectedRowKeys);
            if (response.status === 200) {
                messageApi.success('Báo mất sách hàng loạt thành công!');
                setSelectedRowKeys([]);
                setIsLostModalOpen(false);
                handleChangePage(1);
            }
        } catch (error) {
            messageApi.error(`Lỗi khi báo mất sách hàng loạt: ${error.response?.data?.message || error.message}`);
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
                const response = await getBookBorrows(params);
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
            title: 'Mã cá biệt',
            dataIndex: 'bookCode',
            key: 'bookCode',
        },
        {
            title: 'Nhan đề',
            dataIndex: 'title',
            key: 'title',
        },
        {
            title: 'Số phiếu mượn',
            dataIndex: 'receiptNumber',
            key: 'receiptNumber',
        },
        {
            title: 'Số thẻ',
            dataIndex: 'cardNumber',
            key: 'cardNumber',
        },
        {
            title: 'Tên bạn đọc',
            dataIndex: 'fullName',
            key: 'fullName',
        },
        {
            title: 'Ngày mượn',
            dataIndex: 'borrowDate',
            key: 'borrowDate',
        },
        {
            title: 'Ngày hẹn trả',
            dataIndex: 'dueDate',
            key: 'dueDate',
        },
        {
            title: 'Thao tác',
            key: 'action',
            render: (text, record) => (
                <Space>
                    <Button type="link" onClick={() => handleReturnBook(record.id)}>
                        Trả sách
                    </Button>
                    <Button type="link" onClick={() => handleReportLostBook(record.id)} danger>
                        Báo mất
                    </Button>
                </Space>
            ),
        },
    ];

    const rowSelection = {
        onChange: (selectedRowKeys) => {
            setSelectedRowKeys(selectedRowKeys);
        },
        getCheckboxProps: (record, index) => ({
            name: record.id,
        }),
    };

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

            <Modal
                title="Thông báo"
                style={{
                    top: 20,
                }}
                open={isReturnModalOpen}
                onOk={() => handleBulkReturnBooks()}
                onCancel={() => setIsReturnModalOpen(false)}
            >
                <span> {selectedRowKeys.length} sách sẽ được trả về thư viện?</span>
            </Modal>

            <Modal
                title="Thông báo"
                style={{
                    top: 20,
                }}
                open={isLostModalOpen}
                onOk={() => handleBulkReportLostBooks()}
                onCancel={() => setIsLostModalOpen(false)}
            >
                <span> {selectedRowKeys.length} sách sẽ được báo mất?</span>
            </Modal>

            <h2>Trả sách</h2>

            <Flex wrap justify="space-between" align="center">
                <Space>
                    <Button onClick={() => setIsReturnModalOpen(true)}>Trả sách</Button>
                    <Button onClick={() => setIsLostModalOpen(true)} danger>
                        Báo mất sách
                    </Button>
                </Space>
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
            </Flex>

            <Table
                bordered
                rowKey="id"
                scroll={{ x: 'max-content' }}
                dataSource={entityData}
                columns={columns}
                rowSelection={rowSelection}
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

export default ReturnRenewBook;
