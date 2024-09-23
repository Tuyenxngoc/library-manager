import { Button, Input, Select, Space } from 'antd';
import { FaSearch } from 'react-icons/fa';

const searchFieldOptions = [
    {
        label: 'Mọi trường',
        value: 'all',
    },
    {
        label: 'Nhan đề',
        value: 'title',
    },
    {
        label: 'Tác giả',
        value: 'author',
    },
    {
        label: 'Nhà xuất bản',
        value: 'publisher',
    },
    {
        label: 'Năm xuất bản',
        value: 'publicationYear',
    },
    {
        label: 'Số ISBN',
        value: 'isbn',
    },
];

const conditionOptions = [
    {
        value: 'and',
        label: 'Và',
    },
    {
        value: 'or',
        label: 'Hoặc',
    },
];

const comparisonOptions = [
    {
        value: 'contains',
        label: 'Chứa',
    },
    {
        value: 'exact',
        label: 'Chính xác',
    },
    {
        value: 'starts_with',
        label: 'Bắt đầu bằng',
    },
    {
        value: 'ends_with',
        label: 'Kết thúc bằng',
    },
];

function AdvancedSearchForm() {
    return (
        <form>
            <div className="">
                <Space className="mb-2">
                    <div style={{ width: 120 }}></div>
                    <Input id="itemCode" name="itemCode" size="large" placeholder="Nhập từ khóa" />
                    <Select size="large" defaultValue="contains" style={{ width: 100 }} options={comparisonOptions} />
                    <span>Trong</span>
                    <Select size="large" defaultValue="all" style={{ width: 150 }} options={searchFieldOptions} />
                </Space>

                <Space className="mb-2">
                    <Select size="large" defaultValue="and" style={{ width: 120 }} options={conditionOptions} />
                    <Input id="itemCode" name="itemCode" size="large" placeholder="Nhập từ khóa" />
                    <Select size="large" defaultValue="contains" style={{ width: 100 }} options={comparisonOptions} />
                    <span>Trong</span>
                    <Select size="large" defaultValue="all" style={{ width: 150 }} options={searchFieldOptions} />
                </Space>

                <Space className="mb-2">
                    <Select size="large" defaultValue="and" style={{ width: 120 }} options={conditionOptions} />
                    <Input id="itemCode" name="itemCode" size="large" placeholder="Nhập từ khóa" />
                    <Select size="large" defaultValue="contains" style={{ width: 100 }} options={comparisonOptions} />
                    <span>Trong</span>
                    <Select size="large" defaultValue="all" style={{ width: 150 }} options={searchFieldOptions} />
                </Space>
            </div>

            <div>
                <Button size="large" type="primary" htmlType="submit" icon={<FaSearch />}>
                    Tìm kiếm
                </Button>
            </div>
        </form>
    );
}

export default AdvancedSearchForm;
