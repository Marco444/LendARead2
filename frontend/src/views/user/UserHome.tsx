import {useState, useEffect, useContext} from 'react';
import { useTranslation } from 'react-i18next';
import MyBooksTable from '../../components/user/MyBooksTable.tsx';
import LendedBooksTable from "../../components/user/LendedBooksTable.tsx";

const UserHomeView = () => {

    const { t } = useTranslation();
    const [table, setTable] = useState('my_books');

    const handleTableChange = (newTable: any) => {
        setTable(newTable);
        // Additional logic if needed
    };

    return (
        <div>
            <div  style={{width: '1000px', margin: 'auto', paddingBottom: '50px', paddingTop: '50px'}}>
                <h1>{t('greeting', { userEmail: 'user@example.com' })}</h1>
                <div style={{ display: 'flex', flexDirection: 'row', marginBottom: "20px" }}>
                    <div style={{ flex: 1, marginRight: '20px' }}>
                        <div className="list-group">
                                <button onClick={() => handleTableChange('my_books')} className={`list-group-item list-group-item-action button-select ${table === 'my_books' ? 'button-select-active' : ''}`}>
                                    {t('my_books')}
                                </button>
                                <button onClick={() => handleTableChange('lended_books')} className={`list-group-item list-group-item-action button-select ${table === 'lended_books' ? 'button-select-active' : ''}`}>
                                    {t('lended_books')}
                                </button>
                                <button onClick={() => handleTableChange('borrowed_books')} className={`list-group-item list-group-item-action button-select ${table === 'borrowed_books' ? 'button-select-active' : ''}`}>
                                    {t('borrowed_books')}
                                </button>
                            </div>
                        </div>
                    <div style={{ flex: 3 }}>
                            {table === 'my_books' && <MyBooksTable />}
                            {table === 'lended_books' && <LendedBooksTable isLender={true}/>}
                            {table === 'borrowed_books' && <LendedBooksTable isLender={false}/>}
                        </div>
                    </div>
                </div>
        </div>
    );
};

export default UserHomeView;
