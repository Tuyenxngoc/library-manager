import { useEffect, useState } from 'react';
import { Button, Input, Select, Space, Table, Tree } from 'antd';
import { FaPrint } from 'react-icons/fa';
import queryString from 'query-string';
import { INITIAL_FILTERS, INITIAL_META } from '~/common/commonConstants';
import { getBookByBookDefinitions } from '~/services/bookDefinitionService';
import { getCategoryGroupsTree } from '~/services/categoryGroupService';

const options = [
    { value: 'title', label: 'Nhan đề' },
    { value: 'bookCode', label: 'Kí hiệu tên sách' },
];

function BookListByCategory() {
    const [meta, setMeta] = useState(INITIAL_META);
    const [filters, setFilters] = useState(INITIAL_FILTERS);

    const [entityData, setEntityData] = useState(null);
    const [treeData, setTreeData] = useState([]);
    const [expandedKeys, setExpandedKeys] = useState([]);

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

    const onSelectTree = (selectedKeys, info) => {
        if (selectedKeys.length > 0) {
            const selectedKey = selectedKeys[0];

            if (selectedKey.startsWith('group-')) {
                const selectedCategoryGroupId = selectedKey.split('-')[1];
                if (selectedCategoryGroupId > 0) {
                    setFilters((prev) => ({
                        ...prev,
                        pageNum: 1,
                        categoryGroupId: selectedCategoryGroupId,
                        categoryId: null,
                    }));
                } else {
                    setFilters((prev) => ({
                        ...prev,
                        pageNum: 1,
                        categoryGroupId: null,
                        categoryId: null,
                    }));
                }
            } else if (selectedKey.startsWith('category-')) {
                const selectedCategoryId = selectedKey.split('-')[1];
                setFilters((prev) => ({
                    ...prev,
                    pageNum: 1,
                    categoryId: selectedCategoryId,
                    categoryGroupId: null,
                }));
            }
        }
    };

    const transformToTreeData = (data) => {
        return data.map((group) => ({
            title: (
                <>
                    {group.name} <b>({group.count})</b>
                </>
            ),
            key: `group-${group.id}`,
            children: group.categories.map((category) => ({
                title: (
                    <>
                        {category.name} <b>({category.count})</b>
                    </>
                ),
                key: `category-${category.id}`,
            })),
        }));
    };

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const params = queryString.stringify(filters);
                const response = await getBookByBookDefinitions(params);
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

    useEffect(() => {
        const fetchTreeData = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const response = await getCategoryGroupsTree();
                const transformedTreeData = transformToTreeData(response.data.data);
                setTreeData(transformedTreeData);

                const allKeys = transformedTreeData.flatMap((group) => {
                    const groupKey = group.key;
                    const categoryKeys = group.children ? group.children.map((child) => child.key) : [];
                    return [groupKey, ...categoryKeys];
                });
                setExpandedKeys(allKeys);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchTreeData();
    }, []);

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
            dataIndex: 'classificationSymbol',
            key: 'classificationSymbol',
            sorter: true,
            showSorterTooltip: false,
            render: (text, record) => <>{text ? text.name : ''}</>,
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
            dataIndex: 'totalBooks',
            key: 'totalBooks',
        },
        {
            title: 'Còn',
            children: [
                {
                    title: 'Trong thư viện',
                    dataIndex: 'availableBooks',
                    key: 'availableBooks',
                    align: 'center',
                    width: 100,
                },
                {
                    title: 'Đang cho mượn',
                    dataIndex: 'borrowedBooks',
                    key: 'borrowedBooks',
                    align: 'center',
                    width: 100,
                },
            ],
        },
        {
            title: 'Đã mất',
            dataIndex: 'lostBooks',
            key: 'lostBooks',
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
                    <Tree
                        treeData={treeData}
                        expandedKeys={expandedKeys}
                        onExpand={setExpandedKeys}
                        onSelect={onSelectTree}
                    />
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
