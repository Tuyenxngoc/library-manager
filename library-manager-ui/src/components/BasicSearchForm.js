import { Button, Input } from 'antd';
import { FaSearch } from 'react-icons/fa';

function BasicSearchForm() {
    return (
        <form className="w-50">
            <div className="mb-2">
                <label htmlFor="itemCode">Mã ấn phẩm:</label>
                <Input id="itemCode" name="itemCode" size="large" placeholder="Nhập mã ấn phẩm" />
            </div>

            <div className="mb-2">
                <label htmlFor="title">Nhan đề:</label>
                <Input id="title" name="title" size="large" placeholder="Nhập nhan đề" />
            </div>

            <div className="mb-2">
                <label htmlFor="keywords">Từ khóa:</label>
                <Input id="keywords" name="keywords" size="large" placeholder="Nhập từ khóa" />
            </div>

            <div className="mb-2">
                <label htmlFor="usageYear">Năm sử dụng:</label>
                <Input id="usageYear" name="usageYear" size="large" placeholder="Nhập năm sử dụng" />
            </div>

            <div className="mb-2">
                <label htmlFor="publicationYear">Năm xuất bản:</label>
                <Input id="publicationYear" name="publicationYear" size="large" placeholder="Nhập năm xuất bản" />
            </div>

            <div className="mb-2">
                <label htmlFor="authorName">Tên tác giả:</label>
                <Input id="authorName" name="authorName" size="large" placeholder="Nhập tên tác giả" />
            </div>

            <Button size="large" type="primary" htmlType="submit" icon={<FaSearch />}>
                Tìm kiếm
            </Button>
        </form>
    );
}

export default BasicSearchForm;
