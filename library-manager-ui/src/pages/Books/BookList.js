import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Flex, Input, Select, Space, Table } from 'antd';
import { FaPrint } from 'react-icons/fa';
import queryString from 'query-string';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { getBooks } from '~/services/bookService';

const options = [{ value: 'bookCode', label: 'Số ĐKCB' }];

function BookList() {
    const navigate = useNavigate();

    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);

    const [searchInput, setSearchInput] = useState('');
    const [activeFilterOption, setActiveFilterOption] = useState(options[0].value);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

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

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getBooks(params);
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
            title: 'Số ĐKCB',
            dataIndex: 'bookCode',
            key: 'bookCode',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Nhan đề',
            dataIndex: 'bookDefinition',
            key: 'bookDefinition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => <span>{text.title}</span>,
        },
        {
            title: 'KHPL',
            dataIndex: 'bookDefinition',
            key: 'bookDefinition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => <span>{text.bookCode}</span>,
        },
        {
            title: 'Tác giả',
            dataIndex: 'bookDefinition',
            key: 'bookDefinition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => (
                <span>
                    {text.authors.map((author, index) => (
                        <span key={author.id}>
                            {author.name}
                            {index < text.authors.length - 1 ? ', ' : ''}
                        </span>
                    ))}
                </span>
            ),
        },
        {
            title: 'Nhà xuất bản',
            dataIndex: 'bookDefinition',
            key: 'bookDefinition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => <span>{text.publisher ? text.publisher.name : 'Không có'}</span>,
        },
        {
            title: 'Năm xuất bản',
            dataIndex: 'bookDefinition',
            key: 'bookDefinition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => text.publishingYear,
        },
        {
            title: 'Tình trạng',
            dataIndex: 'bookCondition',
            key: 'bookCondition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => text || '',
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
            <Flex wrap justify="space-between" align="center">
                <h2>Danh sách sách</h2>
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

                    <Button type="primary" icon={<FaPrint />}>
                        In
                    </Button>
                </Space>
            </Flex>

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

export default BookList;
