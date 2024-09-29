import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import queryString from 'query-string';
import { Button, Image, Input, message, Select, Space, Upload } from 'antd';
import images from '~/assets';
import { getCategories } from '~/services/categoryService';
import { getPublishers } from '~/services/publisherService';
import { getBookSets } from '~/services/bookSetService';
import { handleError } from '~/utils/errorHandler';
import { checkIdIsNumber } from '~/utils/helper';
import { createBookDefinition, getBookDefinitionById, updateBookDefinition } from '~/services/bookDefinitionService';
import { getAuthors } from '~/services/authorService';
import { getClassificationSymbols } from '~/services/classificationSymbolService';
const { TextArea } = Input;
const { Option } = Select;

const defaultValue = {
    title: '',
    categoryId: null,
    authorIds: null,
    classificationSymbolId: null,
    publisherId: null,
    publicationPlace: '',
    bookCode: '',
    publishingYear: '',
    edition: '',
    pageCount: null,
    price: null,
    referencePrice: null,
    bookSize: '',
    parallelTitle: '',
    subtitle: '',
    additionalMaterial: '',
    summary: '',
    isbn: '',
    keywords: '',
    language: '',
    additionalInfo: '',
    series: '',
    image: null,
};

const validationSchema = yup.object({
    title: yup.string().required('Tiêu đề là bắt buộc'),

    categoryId: yup.number().nullable().required('Danh mục là bắt buộc').typeError('Danh mục phải là số hợp lệ'),

    authorIds: yup
        .array()
        .of(yup.number().nullable().typeError('Tác giả phải là số hợp lệ'))
        .nullable()
        .typeError('Danh sách tác giả không hợp lệ'),

    publisherId: yup.number().nullable().typeError('Nhà xuất bản phải là số hợp lệ'),

    classificationSymbolId: yup.number().nullable().typeError('Kí hiệu phân loại phải là số hợp lệ'),

    publicationPlace: yup.string(),

    bookCode: yup.string(),

    publishingYear: yup
        .number()
        .nullable()
        .typeError('Năm xuất bản phải là số hợp lệ')
        .min(1900, 'Năm xuất bản phải lớn hơn 1900')
        .max(new Date().getFullYear(), `Năm xuất bản không được lớn hơn ${new Date().getFullYear()}`),

    edition: yup.string(),

    pageCount: yup.number().nullable().typeError('Số trang phải là số hợp lệ').min(1, 'Số trang phải ít nhất là 1'),

    price: yup.number().nullable().typeError('Giá bán phải là số hợp lệ').positive('Giá bán phải là số dương'),

    referencePrice: yup
        .number()
        .nullable()
        .typeError('Giá tham khảo phải là số hợp lệ')
        .positive('Giá tham khảo phải là số dương'),

    bookSize: yup.string(),

    parallelTitle: yup.string(),

    subtitle: yup.string(),

    additionalMaterial: yup.string(),

    summary: yup.string(),

    isbn: yup.string().matches(/^(97(8|9))?\d{9}(\d|X)$/, 'ISBN phải đúng định dạng hợp lệ'),

    keywords: yup.string(),

    language: yup.string(),

    additionalInfo: yup.string(),

    series: yup.string(),
});

const BookDefinitionForm = ({ mode }) => {
    const isEditMode = mode === 'edit';
    const isCopyMode = mode === 'copy';
    const { id } = useParams();
    const navigate = useNavigate();
    const [messageApi, contextHolder] = message.useMessage();

    const [categories, setCategories] = useState([]);
    const [isCategoriesLoading, setIsCategoriesLoading] = useState(true);

    const [authors, setAuthors] = useState([]);
    const [isAuthorsLoading, setIsAuthorsLoading] = useState(true);

    const [publishers, setPublishers] = useState([]);
    const [isPublishersLoading, setIsPublishersLoading] = useState(true);

    const [bookSets, setBookSets] = useState([]);
    const [isBookSetsLoading, setIsBookSetsLoading] = useState(true);

    const [classificationSymbols, setClassificationSymbols] = useState([]);
    const [isClassificationSymbolsLoading, setIsClassificationSymbolsLoading] = useState(true);

    const [imageUrl, setImageUrl] = useState('');

    const handleUploadChange = (info) => {
        if (info.file.status === 'done' || info.file.status === 'uploading') {
            const url = URL.createObjectURL(info.file.originFileObj);
            setImageUrl(url);
            formik.setFieldValue('image', info.file.originFileObj);
        }
    };

    const uploadProps = {
        name: 'file',
        accept: 'image/*',
        beforeUpload: (file) => {
            const isImage = file.type.startsWith('image/');
            if (!isImage) {
                alert('Bạn chỉ có thể upload file hình ảnh!');
            }
            return isImage;
        },
        onChange: handleUploadChange,
    };

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            let response;
            if (id) {
                response = await updateBookDefinition(id, values);
            } else {
                response = await createBookDefinition(values);
            }

            if (response.status === 200) {
                messageApi.success(response.data.data.message);
            } else if (response.status === 201) {
                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            handleError(error, formik, messageApi);
        } finally {
            setSubmitting(false);
        }
    };

    const formik = useFormik({
        initialValues: defaultValue,
        validationSchema: validationSchema,
        onSubmit: handleSubmit,
        enableReinitialize: true,
    });

    const fetchCategories = async (keyword = '') => {
        setIsCategoriesLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'categoryName' });
            const response = await getCategories(params);
            const { items } = response.data.data;
            setCategories(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải danh mục.');
        } finally {
            setIsCategoriesLoading(false);
        }
    };

    const fetchAuthors = async (keyword = '') => {
        setIsAuthorsLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'fullName' });
            const response = await getAuthors(params);
            const { items } = response.data.data;
            setAuthors(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải tác giả.');
        } finally {
            setIsAuthorsLoading(false);
        }
    };

    const fetchPublishers = async (keyword = '') => {
        setIsPublishersLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'name' });
            const response = await getPublishers(params);
            const { items } = response.data.data;
            setPublishers(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải nhà xuất bản.');
        } finally {
            setIsPublishersLoading(false);
        }
    };

    const fetchBookSets = async (keyword = '') => {
        setIsBookSetsLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'name' });
            const response = await getBookSets(params);
            const { items } = response.data.data;
            setBookSets(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải bộ sách.');
        } finally {
            setIsBookSetsLoading(false);
        }
    };

    const fetchClassificationSymbols = async (keyword = '') => {
        setIsClassificationSymbolsLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'name' });
            const response = await getClassificationSymbols(params);
            const { items } = response.data.data;
            setClassificationSymbols(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải kí hiệu phân loại.');
        } finally {
            setIsClassificationSymbolsLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
        fetchAuthors();
        fetchPublishers();
        fetchBookSets();
        fetchClassificationSymbols();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (id) {
            if (!checkIdIsNumber(id)) {
                navigate('/admin/book-definition');
                return;
            }

            // Nếu có id, lấy thông tin biên mục để sửa
            getBookDefinitionById(id)
                .then((response) => {
                    const {
                        title,
                        price,
                        isbn,
                        publishingYear,
                        edition,
                        referencePrice,
                        publicationPlace,
                        bookCode,
                        pageCount,
                        bookSize,
                        parallelTitle,
                        summary,
                        subtitle,
                        additionalMaterial,
                        keywords,
                        language,
                        imageUrl,
                        series,
                        additionalInfo,
                        authors,
                        publisher,
                        bookSet,
                        classificationSymbol,
                        category,
                    } = response.data.data;
                    setImageUrl(imageUrl);
                    formik.setValues({
                        title,
                        pageCount,
                        price,
                        referencePrice,
                        publicationPlace,
                        bookCode,
                        publishingYear,
                        edition,
                        bookSize,
                        parallelTitle,
                        subtitle,
                        additionalMaterial,
                        summary,
                        isbn,
                        keywords,
                        language,
                        additionalInfo,
                        series,
                        authorIds: authors ? authors.map((author) => author.id) : [],
                        publisherId: publisher?.id,
                        bookSetId: bookSet?.id,
                        categoryId: category.id,
                        classificationSymbolId: classificationSymbol?.id,
                    });
                })
                .catch((error) => {
                    console.log(error);
                });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return (
        <>
            {contextHolder}
            <h2>
                {isEditMode && <h1>Chỉnh sửa biên mục</h1>}
                {isCopyMode && <h1>Nhân bản biên mục</h1>}
                {mode === 'new' && <h1>Thêm mới biên mục</h1>}
            </h2>

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <div className="col-md-10">
                        <div className="row g-3">
                            <div className="col-md-8">
                                <label htmlFor="title">
                                    <span className="text-danger">*</span> Nhan đề:
                                </label>
                                <Input
                                    id="title"
                                    name="title"
                                    value={formik.values.title}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="categoryId">
                                    <span className="text-danger">*</span> Danh mục:
                                </label>
                                <Select
                                    showSearch
                                    allowClear
                                    onSearch={fetchCategories}
                                    filterOption={false}
                                    id="categoryId"
                                    name="categoryId"
                                    value={formik.values.categoryId}
                                    onChange={(value) => formik.setFieldValue('categoryId', value)}
                                    onBlur={() => formik.setFieldTouched('categoryId', true)}
                                    status={formik.touched.categoryId && formik.errors.categoryId ? 'error' : undefined}
                                    loading={isCategoriesLoading}
                                    placeholder="Chọn danh mục"
                                    style={{ width: '100%' }}
                                >
                                    {categories.map((category) => (
                                        <Option key={category.id} value={category.id}>
                                            {category.categoryName}
                                        </Option>
                                    ))}
                                </Select>
                                <div className="text-danger">
                                    {formik.touched.categoryId && formik.errors.categoryId}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="authorIds">Tác giả:</label>
                                <Select
                                    mode="multiple"
                                    showSearch
                                    allowClear
                                    onSearch={fetchAuthors}
                                    filterOption={false}
                                    id="authorIds"
                                    name="authorIds"
                                    value={formik.values.authorIds}
                                    onChange={(value) => formik.setFieldValue('authorIds', value)}
                                    onBlur={() => formik.setFieldTouched('authorIds', true)}
                                    status={formik.touched.authorIds && formik.errors.authorIds ? 'error' : undefined}
                                    loading={isAuthorsLoading}
                                    placeholder="Chọn tác giả"
                                    style={{ width: '100%' }}
                                >
                                    {authors.map((author) => (
                                        <Option key={author.id} value={author.id}>
                                            {author.fullName}
                                        </Option>
                                    ))}
                                </Select>
                                <div className="text-danger">{formik.touched.authorIds && formik.errors.authorIds}</div>{' '}
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="publisherId">Nhà xuất bản:</label>
                                <Select
                                    showSearch
                                    allowClear
                                    onSearch={fetchPublishers}
                                    filterOption={false}
                                    id="publisherId"
                                    name="publisherId"
                                    value={formik.values.publisherId}
                                    onChange={(value) => formik.setFieldValue('publisherId', value)}
                                    onBlur={() => formik.setFieldTouched('publisherId', true)}
                                    status={
                                        formik.touched.publisherId && formik.errors.publisherId ? 'error' : undefined
                                    }
                                    loading={isPublishersLoading}
                                    placeholder="Chọn nhà xuất bản"
                                    style={{ width: '100%' }}
                                >
                                    {publishers.map((publisher) => (
                                        <Option key={publisher.id} value={publisher.id}>
                                            {publisher.name}
                                        </Option>
                                    ))}
                                </Select>
                                <div className="text-danger">
                                    {formik.touched.publisherId && formik.errors.publisherId}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="publicationPlace">Nơi xuất bản:</label>
                                <Input
                                    id="publicationPlace"
                                    name="publicationPlace"
                                    value={formik.values.publicationPlace}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={
                                        formik.touched.publicationPlace && formik.errors.publicationPlace
                                            ? 'error'
                                            : undefined
                                    }
                                />
                                <div className="text-danger">
                                    {formik.touched.publicationPlace && formik.errors.publicationPlace}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="classificationSymbolId">Kí hiệu phân loại:</label>
                                <Select
                                    showSearch
                                    allowClear
                                    onSearch={fetchBookSets}
                                    filterOption={false}
                                    id="classificationSymbolId"
                                    name="classificationSymbolId"
                                    value={formik.values.classificationSymbolId}
                                    onChange={(value) => formik.setFieldValue('classificationSymbolId', value)}
                                    onBlur={() => formik.setFieldTouched('classificationSymbolId', true)}
                                    status={
                                        formik.touched.classificationSymbolId && formik.errors.classificationSymbolId
                                            ? 'error'
                                            : undefined
                                    }
                                    loading={isClassificationSymbolsLoading}
                                    placeholder="Chọn kí hiệu phân loại"
                                    style={{ width: '100%' }}
                                >
                                    {classificationSymbols.map((cs) => (
                                        <Option key={cs.id} value={cs.id}>
                                            {cs.name}
                                        </Option>
                                    ))}
                                </Select>
                                <div className="text-danger">
                                    {formik.touched.classificationSymbolId && formik.errors.classificationSymbolId}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="bookCode">Kí hiệu tên sách:</label>
                                <Input
                                    id="bookCode"
                                    name="bookCode"
                                    value={formik.values.bookCode}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.bookCode && formik.errors.bookCode ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.bookCode && formik.errors.bookCode}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="publishingYear">Năm xuất bản:</label>
                                <Input
                                    id="publishingYear"
                                    name="publishingYear"
                                    value={formik.values.publishingYear}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={
                                        formik.touched.publishingYear && formik.errors.publishingYear
                                            ? 'error'
                                            : undefined
                                    }
                                />
                                <div className="text-danger">
                                    {formik.touched.publishingYear && formik.errors.publishingYear}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="edition">Lần xuất bản:</label>
                                <Input
                                    id="edition"
                                    name="edition"
                                    value={formik.values.edition}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.edition && formik.errors.edition ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.edition && formik.errors.edition}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="pageCount">Số trang:</label>
                                <Input
                                    id="pageCount"
                                    name="pageCount"
                                    value={formik.values.pageCount}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.pageCount && formik.errors.pageCount ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.pageCount && formik.errors.pageCount}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="price">Giá bìa:</label>
                                <Input
                                    id="price"
                                    name="price"
                                    value={formik.values.price}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.price && formik.errors.price ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.price && formik.errors.price}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="referencePrice">Giá tham khảo:</label>
                                <Input
                                    id="referencePrice"
                                    name="referencePrice"
                                    value={formik.values.referencePrice}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={
                                        formik.touched.referencePrice && formik.errors.referencePrice
                                            ? 'error'
                                            : undefined
                                    }
                                />
                                <div className="text-danger">
                                    {formik.touched.referencePrice && formik.errors.referencePrice}
                                </div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="bookSize">Khổ sách:</label>
                                <Input
                                    id="bookSize"
                                    name="bookSize"
                                    value={formik.values.bookSize}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    status={formik.touched.bookSize && formik.errors.bookSize ? 'error' : undefined}
                                />
                                <div className="text-danger">{formik.touched.bookSize && formik.errors.bookSize}</div>
                            </div>

                            <div className="col-md-4">
                                <label htmlFor="bookSetId">Bộ sách:</label>
                                <Select
                                    showSearch
                                    allowClear
                                    onSearch={fetchBookSets}
                                    filterOption={false}
                                    id="bookSetId"
                                    name="bookSetId"
                                    value={formik.values.bookSetId}
                                    onChange={(value) => formik.setFieldValue('bookSetId', value)}
                                    onBlur={() => formik.setFieldTouched('bookSetId', true)}
                                    status={formik.touched.bookSetId && formik.errors.bookSetId ? 'error' : undefined}
                                    loading={isBookSetsLoading}
                                    placeholder="Chọn bộ sách"
                                    style={{ width: '100%' }}
                                >
                                    {bookSets.map((bookset) => (
                                        <Option key={bookset.id} value={bookset.id}>
                                            {bookset.name}
                                        </Option>
                                    ))}
                                </Select>
                                <div className="text-danger">{formik.touched.bookSetId && formik.errors.bookSetId}</div>
                            </div>
                        </div>
                    </div>

                    <div className="col-md-2 text-center">
                        <Image width={200} src={imageUrl} fallback={images.placeimg} />

                        <Upload {...uploadProps}>
                            <Button type="text">Chọn ảnh</Button>
                        </Upload>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="parallelTitle">Nhan đề song song:</label>
                        <Input
                            id="parallelTitle"
                            name="parallelTitle"
                            value={formik.values.parallelTitle}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.parallelTitle && formik.errors.parallelTitle ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.parallelTitle && formik.errors.parallelTitle}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="subtitle">Phụ đề:</label>
                        <Input
                            id="subtitle"
                            name="subtitle"
                            value={formik.values.subtitle}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.subtitle && formik.errors.subtitle ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.subtitle && formik.errors.subtitle}</div>
                    </div>

                    <div className="col-md-4">
                        <label htmlFor="additionalMaterial">Tài liệu đi kèm:</label>
                        <Input
                            id="additionalMaterial"
                            name="additionalMaterial"
                            value={formik.values.additionalMaterial}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={
                                formik.touched.additionalMaterial && formik.errors.additionalMaterial
                                    ? 'error'
                                    : undefined
                            }
                        />
                        <div className="text-danger">
                            {formik.touched.additionalMaterial && formik.errors.additionalMaterial}
                        </div>
                    </div>

                    <div className="col-md-4">
                        <label htmlFor="isbn">Mã ISBN:</label>
                        <Input
                            id="isbn"
                            name="isbn"
                            value={formik.values.isbn}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.isbn && formik.errors.isbn ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.isbn && formik.errors.isbn}</div>
                    </div>

                    <div className="col-md-4">
                        <label htmlFor="language">Ngôn ngữ:</label>
                        <Input
                            id="language"
                            name="language"
                            value={formik.values.language}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.language && formik.errors.language ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.language && formik.errors.language}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="additionalInfo">Thông tin khác:</label>
                        <Input
                            id="additionalInfo"
                            name="additionalInfo"
                            value={formik.values.additionalInfo}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.additionalInfo && formik.errors.additionalInfo ? 'error' : undefined}
                        />
                        <div className="text-danger">
                            {formik.touched.additionalInfo && formik.errors.additionalInfo}
                        </div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="series">Tùng thư:</label>
                        <Input
                            id="series"
                            name="series"
                            value={formik.values.series}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.series && formik.errors.series ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.series && formik.errors.series}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="summary">Tóm tắt:</label>
                        <TextArea
                            rows={4}
                            id="summary"
                            name="summary"
                            value={formik.values.summary}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.summary && formik.errors.summary ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.summary && formik.errors.summary}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="keywords">Từ khóa tìm kiếm:</label>
                        <TextArea
                            rows={4}
                            id="keywords"
                            name="keywords"
                            value={formik.values.keywords}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.keywords && formik.errors.keywords ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.keywords && formik.errors.keywords}</div>
                    </div>

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/book-definition')}>Quay lại</Button>
                            <Button type="primary" htmlType="submit" loading={formik.isSubmitting}>
                                Lưu
                            </Button>
                        </Space>
                    </div>
                </div>
            </form>
        </>
    );
};

export default BookDefinitionForm;
