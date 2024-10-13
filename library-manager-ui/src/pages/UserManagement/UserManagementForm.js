import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import queryString from 'query-string';
import dayjs from 'dayjs';
import { Button, DatePicker, message, Space } from 'antd';
import { handleError } from '~/utils/errorHandler';
import FormInput from '~/components/FormInput';
import FormTextArea from '~/components/FormTextArea';
import FormSelect from '~/components/FormSelect';
import { getUserGroups } from '~/services/userGroupService';
import { createUser, getUserById, updateUser } from '~/services/userService';

const statusOptions = [
    { value: 'ACTIVATED', label: 'Đã kích hoạt' },
    { value: 'DEACTIVATED', label: 'Chưa kích hoạt' },
    { value: 'SUSPENDED', label: 'Tạm dừng' },
];

const defaultValue = {
    username: '',
    password: '',
    userGroupId: null,
    expiryDate: null,
    status: 'ACTIVATED',
    fullName: '',
    position: '',
    email: '',
    phoneNumber: '',
    address: '',
    note: '',
};

const validationSchema = yup.object({
    username: yup
        .string()
        .matches(/^[a-z][a-z0-9]{3,15}$/, 'Tên đăng nhập không đúng định dạng')
        .required('Tên đăng nhập là bắt buộc'),

    password: yup
        .string()
        .matches(/^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,}$/, 'Mật khẩu không đúng định dạng')
        .required('Mật khẩu là bắt buộc'),

    phoneNumber: yup
        .string()
        .matches(/^(?:\+84|0)(?:1[2689]|9[0-9]|3[2-9]|5[6-9]|7[0-9])(?:\d{7}|\d{8})$/, 'Số điện thoại không hợp lệ')
        .required('Số điện thoại là bắt buộc'),

    fullName: yup
        .string()
        .matches(/^\S+(\s+\S+)+/, 'Họ tên không đúng định dạng')
        .required('Họ tên là bắt buộc'),

    email: yup.string().email('Email không hợp lệ').required('Email là bắt buộc'),

    userGroupId: yup.number().required('Nhóm người dùng là bắt buộc'),

    expiryDate: yup.date().nullable(),

    status: yup.string().nullable(),

    position: yup.string().nullable(),

    address: yup.string().nullable(),

    note: yup.string().nullable(),
});

const UserManagementForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [messageApi, contextHolder] = message.useMessage();

    const [userGroups, setUserGroups] = useState([]);
    const [isUserGroupsLoading, setIsUserGroupsLoading] = useState(true);

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            let response;
            if (id) {
                response = await updateUser(id, values);
            } else {
                response = await createUser(values);
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

    const fetchUserGroups = async (keyword = '') => {
        setIsUserGroupsLoading(true);
        try {
            const params = queryString.stringify({ keyword, searchBy: 'name', activeFlag: true });
            const response = await getUserGroups(params);
            const { items } = response.data.data;
            setUserGroups(items);
        } catch (error) {
            messageApi.error(error.message || 'Có lỗi xảy ra khi tải nhóm người dùng.');
        } finally {
            setIsUserGroupsLoading(false);
        }
    };

    useEffect(() => {
        fetchUserGroups();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        // Nếu có id, lấy thông tin người dùng để sửa
        if (id) {
            getUserById(id)
                .then((response) => {
                    const {
                        username,
                        password,
                        userGroup,
                        expiryDate,
                        status,
                        fullName,
                        position,
                        email,
                        phoneNumber,
                        address,
                        note,
                    } = response.data.data;
                    formik.setValues({
                        username,
                        password,
                        userGroupId: userGroup ? userGroup.id : null,
                        expiryDate: expiryDate ? dayjs(expiryDate) : null,
                        status,
                        fullName,
                        position,
                        email,
                        phoneNumber,
                        address,
                        note,
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

            {id ? <h2>Chỉnh sửa người dùng</h2> : <h2>Thêm mới người dùng</h2>}

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <FormInput
                        id="username"
                        label="Tên đăng nhập"
                        className={'col-md-6'}
                        formik={formik}
                        required
                        autoComplete="on"
                    />

                    <FormInput id="password" label="Mật khẩu" className={'col-md-6'} formik={formik} required />

                    <FormSelect
                        required
                        id="userGroupId"
                        label="Nhóm người dùng"
                        className="col-md-6"
                        formik={formik}
                        options={userGroups}
                        loading={isUserGroupsLoading}
                        onSearch={fetchUserGroups}
                        fieldNames={{ label: 'name', value: 'id' }}
                    />

                    <div className="col-md-6">
                        <label htmlFor="expiryDate">Hiệu lực đến ngày:</label>
                        <div>
                            <DatePicker
                                className="w-100"
                                id="expiryDate"
                                name="expiryDate"
                                value={formik.values.expiryDate}
                                onChange={(date) => formik.setFieldValue('expiryDate', date)}
                                status={formik.touched.expiryDate && formik.errors.expiryDate ? 'error' : undefined}
                            />
                        </div>
                        <div className="text-danger">{formik.touched.expiryDate && formik.errors.expiryDate}</div>
                    </div>

                    <FormSelect
                        required
                        id="status"
                        label="Trạng thái"
                        className="col-md-6"
                        formik={formik}
                        options={statusOptions}
                    />

                    <FormInput id="fullName" label="Họ tên" formik={formik} className="col-md-6" required />

                    <FormInput
                        id="email"
                        label="Email"
                        formik={formik}
                        className="col-md-6"
                        required
                        autoComplete="on"
                    />

                    <FormInput id="phoneNumber" label="Số điện thoại" formik={formik} className="col-md-6" required />

                    <FormInput id="position" label="Chức vụ" formik={formik} className="col-md-6" />

                    <FormInput id="address" label="Địa chỉ" formik={formik} className="col-md-6" autoComplete="on" />

                    <FormTextArea id="note" label="Ghi chú" formik={formik} className="col-md-12" />

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/users')}>Quay lại</Button>
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

export default UserManagementForm;
