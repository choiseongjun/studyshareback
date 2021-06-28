package study.share.com.source.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.share.com.source.model.DTO.ReportDTO;
import study.share.com.source.model.Report;
import study.share.com.source.repository.report.ReportRepository;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public Report saveReport(ReportDTO reportDTO)
    {
        Report report= Report.createBuilder()
                .reportDTO(reportDTO)
                .build();
        return reportRepository.save(report);
    }

}
