import { Parallax } from 'react-parallax';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';

function HolidaySchedule() {
    const items = [
        {
            label: 'Trang chủ',
            url: '/',
        },
        {
            label: 'Lịch nghỉ lễ',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Về chúng tôi</h1>
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
                <div className="row">
                    <div className="col-12 mb-4">
                        <SectionHeader title={<h2 className="mb-0">Lịch nghỉ lễ</h2>} subtitle="Lịch nghỉ" />
                    </div>
                    <div className="col-12">
                        <table
                            className="table table-striped table-responsive table-bordered table-hover KAP"
                            id="dataTables-sKyNghiNgayLe"
                        >
                            <thead>
                                <tr role="row">
                                    <th>Tên kỳ nghỉ</th>
                                    <th>Ngày bắt đầu</th>
                                    <th>Nghỉ hết ngày</th>
                                    <th>Số ngày nghỉ</th>
                                </tr>
                            </thead>
                            <tbody id="tblData">
                                <tr>
                                    <td id="KNTen">Ngày nhà giáo Việt N...</td>
                                    <td id="startDate">20/11/2023</td>
                                    <td id="endDate">20/11/2023</td>
                                    <td id="songaynghi">1</td>
                                </tr>
                                <tr>
                                    <td id="KNTen">Tết Nguyên Đán</td>
                                    <td id="startDate">07/02/2024</td>
                                    <td id="endDate">15/02/2024</td>
                                    <td id="songaynghi">9</td>
                                </tr>
                                <tr>
                                    <td id="KNTen">Ngày, Giải phóng MN,...</td>
                                    <td id="startDate">30/04/2024</td>
                                    <td id="endDate">01/05/2024</td>
                                    <td id="songaynghi">2</td>
                                </tr>
                                <tr>
                                    <td id="KNTen">KỲ NGHỈ TẾT DƯƠNG</td>
                                    <td id="startDate">01/01/2024</td>
                                    <td id="endDate">02/01/2024</td>
                                    <td id="songaynghi">2</td>
                                </tr>
                                <tr>
                                    <td id="KNTen">tết âm</td>
                                    <td id="startDate">15/02/2024</td>
                                    <td id="endDate">20/02/2024</td>
                                    <td id="songaynghi">6</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </>
    );
}

export default HolidaySchedule;
