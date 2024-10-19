import { Button } from 'antd';
import { useEffect, useState } from 'react';
import { Parallax } from 'react-parallax';
import { useParams } from 'react-router-dom';
import { backgrounds } from '~/assets';
import Breadcrumb from '~/components/Breadcrumb';
import SectionHeader from '~/components/SectionHeader';
import { getNewsArticleByTitleSlugForUser } from '~/services/newsArticlesService';

function NewsArticleDetail() {
    const { id } = useParams();

    const [entityData, setEntityData] = useState(null);

    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
        const fetchEntities = async () => {
            setIsLoading(true);
            setErrorMessage(null);
            try {
                const response = await getNewsArticleByTitleSlugForUser(id);
                const { data } = response.data;
                setEntityData(data);
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
            label: 'Chi tiết bài viết',
        },
    ];

    return (
        <>
            <Parallax bgImage={backgrounds.bgparallax7} strength={500}>
                <div className="innerbanner">
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-auto">
                                <h1>Chi tiết bài viết</h1>
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
                    <div className="col-3">
                        <Button block>Phân loại tin tức</Button>
                    </div>
                    <div className="col-9">
                        {isLoading ? (
                            <>loading</>
                        ) : errorMessage ? (
                            <>error</>
                        ) : (
                            <>
                                <figure className="newsdetailimg">
                                    <img
                                        style={{ height: '389.73px', width: '100%', objectFit: 'cover' }}
                                        src="/Images/TinTuc/3.2_cah_ly.jpg"
                                        alt=" description"
                                    />
                                    <figcaption className="author">
                                        <div className="authorinfo">
                                            <span className="bookwriter">
                                                Tác giả:
                                                <a href="javascript:void(0);">admin</a>
                                            </span>
                                            <ul className="postmetadata">
                                                <li>
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-calendar" style={{ color: '#bdbdbd' }} />
                                                        <i style={{ color: '#bdbdbd' }}>03/02/2021</i>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </figcaption>
                                </figure>

                                <div className="tg-newsdetail">
                                    <div className="tg-posttitle">
                                        <h3>
                                            <a href="javascript:void(0);">
                                                Sáng 3/2, Hà Nội và 3 địa phương khác có thêm 9 ca mắc COVID-19 ở cộng
                                                đồng
                                            </a>
                                        </h3>
                                    </div>
                                    <div className="tg-description">
                                        <blockquote style={{ padding: '18px 0px 40px 0px', textAlign: 'left' }}>
                                            <q>
                                                “Bản tin 6h ngày 3/2 của Ban Chỉ đạo Quốc gia Phòng chống dịch COVID-19
                                                cho biết có thêm 9 ca mắc mới COVID-19 ở cộng đồng ghi nhận tại Hải
                                                Dương (2 ca), Hà Nội (1 ca), Gia Lai (4 ca) và Bình Dương (2 ca)”
                                            </q>
                                        </blockquote>
                                        <p>Số ca mắc ở Việt Nam:</p>
                                        <p>
                                            - Tính đến 6h ngày 03/02: Việt Nam có tổng cộng 1003 ca mắc COVID-19 do lây
                                            nhiễm trong nước, trong đó số lượng ca mắc mới tính từ ngày 27/1 đến nay:
                                            310 ca.
                                        </p>
                                        <p>
                                            - Tính từ 18h ngày 02/02 đến 6h ngày 03/02: 9 ca mắc mới, trong đó có 0 ca
                                            nhập cảnh được cách ly ngay.
                                        </p>
                                        <p>
                                            Thông tin ca mắc mới: 09 CA MẮC MỚI (BN1883-1891) là các ca cộng đồng tại
                                            Gia Lai (4), Hải Dương (2), Bình Dương (2) và Hà Nội (1). Cụ thể:
                                        </p>
                                        <p>
                                            <img
                                                alt=""
                                                src="https://media.suckhoedoisong.vn/Images/thaibinh/2021/02/01/sang-1-2-1612133790.jpg"
                                                style={{ height: 600, width: 800 }}
                                            />
                                        </p>
                                        <p>
                                            - Thành phố Hà Nội ghi nhận 01 bệnh nhân (BN1883): 1 F1 của BN1814 liên quan
                                            ổ dịch thành phố Chí Linh, tỉnh Hải Dương.
                                        </p>
                                        <p>
                                            -&nbsp;Tỉnh Hải Dương ghi nhận 02 bệnh nhân (BN1884-1885): là công nhân
                                            thành phố Chí Linh, tỉnh Hải Dương, đã được cách ly, không có nguy cơ lây
                                            nhiễm ra cộng đồng, lấy mẫu xét nghiệm lần đầu âm tính ngày 28/1.
                                        </p>
                                        <p>
                                            - Tỉnh Bình Dương ghi nhận 02 bệnh nhân (BN1886-BN1887): 2 F1 của BN1843,
                                            BN1801 liên quan ổ dịch thành phố Chí Linh, tỉnh Hải Dương.
                                        </p>
                                        <p>
                                            - Tỉnh Gia Lai ghi nhận 04 bệnh nhân (BN1888-1891): 3 F1 liên quan ổ dịch
                                            Công ty POYUN, tỉnh Hải Dương; 1 ca bệnh đang được điều tra dịch tễ.
                                        </p>
                                        <p>
                                            Số người cách ly: Tổng số người tiếp xúc gần và nhập cảnh từ vùng dịch đang
                                            được theo dõi sức khỏe (cách ly): 27.714, trong đó:
                                        </p>
                                        <p>- Cách ly tập trung tại bệnh viện: 227</p>
                                        <p>- Cách ly tập trung tại cơ sở khác: 20.917</p>
                                        <p>- Cách ly tại nhà, nơi lưu trú: 6.570.</p>
                                        <p>
                                            <img
                                                alt=""
                                                src="https://media.suckhoedoisong.vn/Images/toanthang/2021/02/03/3.2_cah_ly.jpg"
                                                style={{ height: 1299, width: 2048 }}
                                            />
                                        </p>
                                        <p>
                                            Tình hình điều trị: Theo báo cáo của Tiểu ban Điều trị Ban chỉ đạo Quốc gia
                                            phòng, chống dịch COVID-19: đến&nbsp;thời điểm này nước ta đã chữa khỏi
                                            1.461 bệnh nhân COVID-19.
                                        </p>
                                        <p>
                                            Tính đến thời điểm này trong số các bệnh nhân COVID-19 đang điều trị tại các
                                            cơ sở y tế, số ca âm tính lần 1 với virus SARS-CoV-2: 3ca; Số ca âm tính lần
                                            2 với SARS-CoV-2: 7 ca, số ca âm tính lần 3 là 2 ca.
                                        </p>
                                        <p>
                                            Trong số các bệnh nhân đang điều trị tại các cơ sở y tế trên cả nước, hiện
                                            BN1536 đang điều trị tại BV Phổi Đà Nặng là bệnh nhân nặng nhất. BN1536 tuổi
                                            cao, có tiền sử tăng huyết áp và đái tháo đường nhiều năm nay.
                                        </p>
                                        <p>
                                            Từ ngày nhập viện (15/1) đến nay, Tiểu ban điều trị và Hội đồng chuyên môn
                                            đã 4 lần tổ chức hội chẩn quốc gia tình hình sức khoẻ của bệnh nhân và yêu
                                            cầu BV Phổi Đà Nẵng theo dõi sát sao trường hợp này. Các bác sĩ và điều
                                            dưỡng có kinh nghiệm về hồi sức tích cực của BV Đà Nẵng cũng đã được điều
                                            động sang BV Phổi để hỗ trợ điều trị cho BN1536.
                                        </p>
                                        <p>
                                            Số ca tử vong liên quan đến COVID-19 ở nước ta đến nay là 35 ca, đây là
                                            những bệnh nhân có nhiều bệnh lý nền nặng, bao gồm tại Đà Nẵng (31 trường
                                            hợp), Quảng Nam (03) và Quảng Trị (01).
                                        </p>
                                        <p>
                                            Liên quan đến công tác sàng lọc bệnh nhân tại các cơ sở y tế, thông tin tại
                                            cuộc họp trực tuyến với các địa phương chống dịch COVID-19 chiều
                                            2/2,&nbsp;PGS.TS Lương Ngọc Khuê, Cục trưởng Cục Quản lý Khám chữa bệnh, Bộ
                                            Y tế cho biết, qua phân tích 240 bệnh nhân COVID-19 mới đợt này cho thấy có
                                            đến 80% bệnh nhân không có triệu chứng. Đây là thách thức lớn với các bệnh
                                            viện trong quá trình sàng lọc bệnh nhân.
                                        </p>
                                        <p>
                                            Trong số các bệnh nhân trong đợt dịch mới này chỉ có 1 bệnh nhân nặng, 3
                                            bệnh nhân phải thở oxy, 20 bệnh nhân có diễn biến bệnh cảnh lâm sàng.
                                        </p>
                                        <p>
                                            Vì vậy, Cục trưởng Lương Ngọc Khuê đề nghị tất cả các bệnh viện quay lại
                                            khai thác kỹ tiền sử dịch tễ tất cả người đến khám.
                                        </p>
                                        <p>
                                            “Nếu cứ đợi bệnh nhân ho, sốt, khó thở thì sẽ dễ bỏ sót ca bệnh, bệnh nhân
                                            sẽ vào giữa bệnh viện”, Cục trưởng Lương Ngọc Khuê cảnh báo và lưu ý trong
                                            điều trị phải luôn chú ý mở cửa thông thoáng các phòng khám, khu điều trị và
                                            đặc biệt lưu ý vấn đề kiểm soát nhiễm khuẩn.
                                        </p>
                                        <p>
                                            Để sống chung an toàn với đại dịch COVID-19, người dân cần tuân thủ thực
                                            hiện nguyên tắc 5K của Bộ Y tế:
                                            <br />
                                            <br />
                                            - Khẩu trang
                                            <br />
                                            <br />
                                            -Khử khuẩn
                                            <br />
                                            <br />
                                            -Khoảng cách
                                            <br />
                                            <br />
                                            -Không tụ tập
                                            <br />
                                            <br />
                                            - Khai báo y tế
                                            <br />
                                            &nbsp;
                                        </p>
                                        <p>&nbsp;</p>
                                        <p>
                                            <img
                                                alt=""
                                                src="https://media.suckhoedoisong.vn/Images/thaibinh/2021/02/03/5K_phien_ban_moi.jpg"
                                                style={{ height: 2048, width: 1344 }}
                                            />
                                        </p>
                                    </div>
                                    <div className="tg-tagsshare">
                                        <div className="tg-socialshare">
                                            <span>Share:</span>
                                            <ul className="tg-socialicons">
                                                <li className="tg-facebook">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-facebook" />
                                                    </a>
                                                </li>
                                                <li className="tg-twitter">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-twitter" />
                                                    </a>
                                                </li>
                                                <li className="tg-linkedin">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-linkedin" />
                                                    </a>
                                                </li>
                                                <li className="tg-googleplus">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-google-plus" />
                                                    </a>
                                                </li>
                                                <li className="tg-rss">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-rss" />
                                                    </a>
                                                </li>
                                                <li className="tg-twitter">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-twitter" />
                                                    </a>
                                                </li>
                                                <li className="tg-linkedin">
                                                    <a href="javascript:void(0);">
                                                        <i className="fa fa-linkedin" />
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default NewsArticleDetail;
