import { useEffect, useState } from 'react';
import { Button, Input } from 'antd';
import { Parallax } from 'react-parallax';
import { FaSearch } from 'react-icons/fa';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';
import { getBorrowReceiptsForReader } from '~/services/borrowReceiptService';

function BorrowHistory() {
    const [entityData, setEntityData] = useState(null);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const response = await getBorrowReceiptsForReader();
                const { items, meta } = response.data.data;
                setEntityData(items);
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
            label: 'Lịch sử mượn trả ấn phẩm',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Lịch sử mượn trả</h1>
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
                    <SectionHeader title={<h2 className="mb-0">Lịch sử mượn trả ấn phẩm</h2>} subtitle="Lịch sử" />
                </div>
                <div className="row mb-4 justify-content-end align-items-center">
                    <div className="col-4">
                        <Input
                            name="search"
                            size="large"
                            placeholder="Nhập nhan số phiếu mượn, MCB..."
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
                                    </div>
                                </div>

                                <div className="col-4">
                                    <div className="text-end">
                                        <span>{entityData.length} mục</span>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-12">
                                    <table className="table table-striped table-bordered table-hover dataTable no-footer dtr-inline KAP">
                                        <thead>
                                            <tr role="row">
                                                <th>STT</th>
                                                <th>Mã phiếu</th>
                                                <th>Số sách</th>
                                                <th>Ngày mượn</th>
                                                <th>Ngày hẹn trả</th>
                                                <th>Ngày trả</th>
                                                <th>Trạng thái</th>
                                                <th>Ghi chú</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {entityData.length > 0 ? (
                                                entityData.map((item, index) => (
                                                    <tr key={item.id || index}>
                                                        <td>{index + 1}</td>
                                                        <td>{item.receiptNumber}</td>
                                                        <td>{item.books}</td>
                                                        <td>{item.borrowDate}</td>
                                                        <td>{item.dueDate}</td>
                                                        <td>{item.returnDate || 'Chưa trả'}</td>
                                                        <td>{item.status}</td>
                                                        <td>{item.note || '-'}</td>
                                                    </tr>
                                                ))
                                            ) : (
                                                <tr>
                                                    <td colSpan="8" style={{ textAlign: 'center' }}>
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

export default BorrowHistory;
