import { useState } from 'react';
import { Button, Input } from 'antd';
import { FaSearch } from 'react-icons/fa';

function BasicSearchForm({ onSearch }) {
    const [formData, setFormData] = useState({
        bookCode: '',
        title: '',
        keyword: '',
        publishingYear: '',
        author: '',
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSearchSubmit = async (e) => {
        e.preventDefault();

        onSearch(formData);
    };

    return (
        <form className="w-50" onSubmit={handleSearchSubmit}>
            <div className="mb-2">
                <label htmlFor="bookCode">Mã ấn phẩm:</label>
                <Input
                    id="bookCode"
                    name="bookCode"
                    size="large"
                    placeholder="Nhập mã ấn phẩm"
                    value={formData.bookCode}
                    onChange={handleInputChange}
                />
            </div>

            <div className="mb-2">
                <label htmlFor="title">Nhan đề:</label>
                <Input
                    id="title"
                    name="title"
                    size="large"
                    placeholder="Nhập nhan đề"
                    value={formData.title}
                    onChange={handleInputChange}
                />
            </div>

            <div className="mb-2">
                <label htmlFor="keyword">Từ khóa:</label>
                <Input
                    id="keyword"
                    name="keyword"
                    size="large"
                    placeholder="Nhập từ khóa"
                    value={formData.keyword}
                    onChange={handleInputChange}
                />
            </div>

            <div className="mb-2">
                <label htmlFor="publishingYear">Năm xuất bản:</label>
                <Input
                    id="publishingYear"
                    name="publishingYear"
                    size="large"
                    placeholder="Nhập năm xuất bản"
                    value={formData.publishingYear}
                    onChange={handleInputChange}
                />
            </div>

            <div className="mb-2">
                <label htmlFor="author">Tên tác giả:</label>
                <Input
                    id="author"
                    name="author"
                    size="large"
                    placeholder="Nhập tên tác giả"
                    value={formData.author}
                    onChange={handleInputChange}
                />
            </div>

            <Button size="large" type="primary" htmlType="submit" icon={<FaSearch />}>
                Tìm kiếm
            </Button>
        </form>
    );
}

export default BasicSearchForm;
