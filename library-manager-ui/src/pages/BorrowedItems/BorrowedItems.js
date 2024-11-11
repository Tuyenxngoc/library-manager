import { Button, Checkbox, Input } from 'antd';
import { Parallax } from 'react-parallax';
import { FaSearch } from 'react-icons/fa';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';
import { useEffect, useState } from 'react';
import { getCartDetails, removeFromCart } from '~/services/cartService';

function BorrowedItems() {
    const [entityData, setEntityData] = useState(null);
    const [selectedItems, setSelectedItems] = useState([]);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    const handleCheckboxChange = (id) => {
        setSelectedItems((prev) => (prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]));
    };

    const handleDelete = async () => {
        try {
            await removeFromCart(selectedItems);
            setEntityData((prev) => prev.filter((item) => !selectedItems.includes(item.id)));
            setSelectedItems([]);
        } catch (error) {
            setErrorMessage('Could not delete selected items.');
        }
    };

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const response = await getCartDetails();
                setEntityData(response.data.data);
            } catch (error) {
                setErrorMessage(error.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchEntities();
    }, []);

    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Thông tin ấn phẩm được đăng ký mượn',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Đăng ký mượn ấn phẩm</h1>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <Breadcrumb items={items} />
                            </div>
                        </div>
                    </div>
                </div>
            </Parallax>

            <div className="container sectionspace">
                <div className="row mb-4">
                    <SectionHeader
                        title={<h2 className="mb-0">Thông tin ấn phẩm được đăng ký</h2>}
                        subtitle="Thông tin mượn"
                    />
                </div>
                <div className="row mb-4 justify-content-end align-items-center">
                    <div className="col-4">
                        <Input
                            name="search"
                            size="large"
                            placeholder="Nhập nhan đề ..."
                            addonAfter={
                                <Button type="text">
                                    <FaSearch />
                                </Button>
                            }
                        />
                    </div>
                </div>

                {isLoading ? (
                    <div>Đang tải dữ liệu...</div>
                ) : errorMessage ? (
                    <div>Lỗi tải dữ liệu: {errorMessage}</div>
                ) : (
                    entityData && (
                        <>
                            <div className="row mb-4 justify-content-between align-items-center">
                                <div className="col-4">
                                    <div className="d-flex justify-content-start align-items-center">
                                        <div className="text-success">Chưa xử lý (1)</div>
                                        <div className="mx-2"> Ι </div>
                                        <div className="text-danger me-2">Quá hạn mượn (0)</div>
                                        <Button type="default" onClick={handleDelete} disabled={!selectedItems.length}>
                                            Xóa
                                        </Button>
                                    </div>
                                </div>

                                <div className="col-4">
                                    <div className="text-end">
                                        <span>1 mục</span>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-12">
                                    <table className="table table-striped table-bordered table-hover dataTable no-footer dtr-inline KAP">
                                        <thead>
                                            <tr role="row">
                                                <th style={{ width: 20, textAlign: 'center' }}>
                                                    <Checkbox
                                                        name="checkbox"
                                                        checked={
                                                            selectedItems.length === entityData.length &&
                                                            entityData.length > 0
                                                        }
                                                        onChange={(e) => {
                                                            setSelectedItems(
                                                                e.target.checked
                                                                    ? entityData.map((item) => item.id)
                                                                    : [],
                                                            );
                                                        }}
                                                    />
                                                </th>
                                                <th style={{ width: 120 }}>Mã nhan đề</th>
                                                <th style={{ width: 150 }}>Nhan đề</th>
                                                <th style={{ width: 134 }}>Tên tác giả</th>
                                                <th style={{ width: 100 }}>Đăng ký mượn từ</th>
                                                <th style={{ width: 100 }}>Đến</th>
                                                <th style={{ width: 87 }}>Trạng thái</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {entityData.length > 0 ? (
                                                entityData.map((item, index) => (
                                                    <tr key={item.bookCode || index}>
                                                        <td style={{ textAlign: 'center' }}>
                                                            <Checkbox
                                                                name="checkbox"
                                                                checked={selectedItems.includes(item.id)}
                                                                onChange={() => handleCheckboxChange(item.id)}
                                                            />
                                                        </td>
                                                        <td>{item.bookCode}</td>
                                                        <td>{item.title}</td>
                                                        <td>
                                                            {item.authors.length > 0
                                                                ? item.authors.join(', ')
                                                                : 'Không xác định'}
                                                        </td>
                                                        <td>{item.borrowFrom}</td>
                                                        <td>{item.borrowTo}</td>
                                                        <td
                                                            style={{
                                                                color:
                                                                    new Date(item.borrowTo) < new Date()
                                                                        ? 'red'
                                                                        : 'green',
                                                            }}
                                                        >
                                                            {new Date(item.borrowTo) < new Date()
                                                                ? 'Hết thời gian mượn'
                                                                : 'Đang mượn'}
                                                        </td>
                                                    </tr>
                                                ))
                                            ) : (
                                                <tr>
                                                    <td colSpan="7" style={{ textAlign: 'center' }}>
                                                        Không có dữ liệu để hiển thị
                                                    </td>
                                                </tr>
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </>
                    )
                )}
            </div>
        </>
    );
}

export default BorrowedItems;
