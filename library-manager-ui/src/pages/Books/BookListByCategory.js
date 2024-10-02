import { useEffect, useState } from 'react';
import { Button, Input, Select, Space, Table, Tree } from 'antd';
import { FaPrint } from 'react-icons/fa';
import queryString from 'query-string';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { getBookDefinitions } from '~/services/bookDefinitionService';

const options = [{ value: 'title', label: 'Nhan đề' }];

function BookListByCategory() {
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
                const response = await getBookDefinitions(params);
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
            title: 'Nhan đề',
            dataIndex: 'title',
            key: 'title',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'KHPL',
            dataIndex: 'bookCode',
            key: 'bookCode',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Tác giả',
            dataIndex: 'authors',
            key: 'authors',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => (
                <span>
                    {text.map((author, index) => (
                        <span key={author.id}>
                            {author.name}
                            {index < text.length - 1 ? ', ' : ''}
                        </span>
                    ))}
                </span>
            ),
        },
        {
            title: 'Nhà xuất bản',
            dataIndex: 'publisher',
            key: 'publisher',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => <span>{text ? text.name : 'Không có'}</span>,
        },
        {
            title: 'Năm xuất bản',
            dataIndex: 'publishingYear',
            key: 'publishingYear',
            sorter: true,
            showSorterTooltip: false,
        },
        {
            title: 'Tổng số bản',
            dataIndex: 'bookCondition',
            key: 'bookCondition',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => text || '',
        },
        {
            title: 'Còn',
            dataIndex: 'bookCondition',
            key: 'bookCondition',
            children: [
                {
                    title: 'Trong thư viện',
                    dataIndex: 'building',
                    key: 'building',
                    align: 'center',
                    width: 100,
                },
                {
                    title: 'Đang cho mượn',
                    dataIndex: 'number',
                    key: 'number',
                    align: 'center',
                    width: 100,
                },
            ],
        },
        {
            title: 'Đã mất',
            dataIndex: 'bookCondition',
            key: 'bookCondition',
            render: (text, record) => text || '',
        },
    ];

    const treeData = [
        {
            title: 'Sách tham khảo',
            key: '0-0',
            children: [
                {
                    title: 'Sách pháp luật',
                    key: '0-0-0',
                },
                {
                    title: 'Sách đạo đức',
                    key: '0-0-1',
                },
                {
                    title: 'parent 1-2',
                    key: '0-0-2',
                },
            ],
        },
        {
            title: 'Sách khác',
            key: '0-1',
            children: [
                {
                    title: 'parent 2-0',
                    key: '0-1-0',
                },
            ],
        },
    ];

    const onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    if (errorMessage) {
        return (
            <div className="alert alert-danger p-2" role="alert">
                Lỗi: {errorMessage}
            </div>
        );
    }
    return (
        <>
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

            <div className="row">
                <div className="col-md-2">
                    <Tree showLine onSelect={onSelect} defaultExpandAll treeData={treeData} />
                </div>
                <div className="col-md-10">
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
            </div>
        </>
    );
}

export default BookListByCategory;
